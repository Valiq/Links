package com.example.cvproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.chrisbanes.photoview.PhotoViewAttacher;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Action extends AppCompatActivity {

    Uri selectedImage;
    int step = 0, count = 0;
    long l;
    String filePath;
    boolean flag, progress = false, progressStop;
    PhotoViewAttacher mAttacher;
    TextView textViewProgress, textViewUse;
    ProgressBar progressBar;
    MyTask Task;
    DatabaseHelper sqlHelper;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                //initializeOpenCVDependencies();
                System.out.println("SUCCESS");
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        if (progress){
            if (!progressStop) {
                textViewProgress.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                textViewProgress.setText(getString(R.string.proc, step, count));
                textViewUse.setText(R.string.Cancel);
                progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            } else {
                textViewProgress.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                textViewProgress.setText(R.string.cancelMes);
                textViewUse.setText(R.string.cancelProcess);
                textViewUse.setEnabled(false);
                progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Task = (MyTask) getLastCustomNonConfigurationInstance();
        System.out.println(Task);
        if (Task != null) {
            System.out.println("Поток " + Task.hashCode());
            Task.link(this);
        }
        setContentView(R.layout.activity_action);
        textViewProgress = findViewById(R.id.Progress);
        textViewUse = findViewById(R.id.Use);
        progressBar = findViewById(R.id.progressBar);
        Intent action = getIntent();
        selectedImage = action.getParcelableExtra("selectedImageUri");
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageURI(selectedImage);
        flag = action.getBooleanExtra("flag", true);
        mAttacher = new PhotoViewAttacher(imageView);
        sqlHelper = new DatabaseHelper(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        super.onRetainCustomNonConfigurationInstance();
        System.out.println("onRetainCustomNonConfigurationInstance");
        if (Task != null) {
            Task.unLink();
        }
        return Task;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onSaveInstanceState (@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("filePath", filePath);
        outState.putParcelable("selectedImage", selectedImage);
        outState.putBoolean("progress",progress);
        outState.putBoolean("progressStop",progressStop);
        outState.putInt("step",step);
        outState.putInt("count",count);
    }

    protected void onRestoreInstanceState(Bundle SavedInstanceState){
        super.onRestoreInstanceState(SavedInstanceState);
        filePath = SavedInstanceState.getString("filePath");
        selectedImage = SavedInstanceState.getParcelable("selectedImage");
        progress = SavedInstanceState.getBoolean("progress");
        progressStop = SavedInstanceState.getBoolean("progressStop");
        step = SavedInstanceState.getInt("step");
        count = SavedInstanceState.getInt("count");
    }

    @SuppressLint("StaticFieldLeak")
    public class MyTask extends AsyncTask<Void, Integer, Void> {

        Action activity;
        String errorMes;
        SQLiteDatabase db;
        Cursor Cursor;
        ArrayList<HashMap<String, Object>> models = new ArrayList<>();
        ArrayList<Integer> arrayId = new ArrayList<>();
        boolean error;

        void link(Action act) {
            activity = act;
        }

        void unLink() {
            activity = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.step = 0;
            error = false;
            activity.textViewProgress.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            activity.textViewProgress.setText(activity.getString(R.string.Start));
            activity.textViewUse.setText(R.string.Cancel);
            activity.progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            activity.progress = true;
            arrayId.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                db =  activity.sqlHelper.open();
                Cursor = db.rawQuery("select " + DatabaseHelper.id + "," + DatabaseHelper.modelimg + " from " + DatabaseHelper.TABLE, null);
                Cursor.moveToFirst();
                count = Cursor.getCount()+2;
                while (!Cursor.isAfterLast()) {
                    if (isCancelled()) return null;
                    HashMap<String, Object> model = new HashMap<>();
                    model.put("id", Cursor.getInt(0));
                    model.put("modelimg", Cursor.getString(1));
                    models.add(model);
                    Cursor.moveToNext();
                }
                Cursor.close();
                db.close();
                    activity.step++;
                    publishProgress(activity.step);
                if (isCancelled()) return null;

                Mat descriptors_scene = new Mat();

                Mat img_scene = Imgcodecs.imread(activity.filePath);

                Mat grayImageScene = new Mat(img_scene.rows(), img_scene.cols(), img_scene.type());
                Imgproc.cvtColor(img_scene, grayImageScene, Imgproc.COLOR_BGRA2GRAY);
                Core.normalize(grayImageScene, grayImageScene, 0, 255, Core.NORM_MINMAX);

                MatOfKeyPoint keypoints_scene = new MatOfKeyPoint();

                FeatureDetector fd = FeatureDetector.create(FeatureDetector.ORB);
                fd.detect(img_scene, keypoints_scene);

                DescriptorExtractor extractor = DescriptorExtractor.create(3);
                extractor.compute(img_scene, keypoints_scene, descriptors_scene);

                    activity.step++;
                    publishProgress(activity.step);
                if (isCancelled()) return null;
                int id;
                for (HashMap<String, Object> model : models) {
                        if (isCancelled()) return null;
                        System.out.println("Массив: " + model.get("id") + "; " + model.get("modelimg"));
                        id = activity.findMatches((String)model.get("modelimg"),(Integer)model.get("id"),descriptors_scene,img_scene,keypoints_scene);
                        if (id != -1) {
                            arrayId.add(id);
                        }
                        activity.step++;
                        publishProgress(activity.step);
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " +  e);
                errorMes = e.toString();
                error = true;
                cancel(true);
                if (isCancelled()) return null;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            activity.textViewProgress.setText(activity.getString(R.string.proc, values[0], activity.count));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            activity.textViewUse.setText(R.string.Use);
            activity.textViewUse.setEnabled(true);
            activity.progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0));
            activity.textViewProgress.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            if (error) {
                Toast toast = Toast.makeText(activity.getApplicationContext(), "Ошибка: " + errorMes, Toast.LENGTH_SHORT);
                toast.show();
            }
            activity.progress = false;
            activity.progressStop = false;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
               // activity.textViewUse.setText(R.string.Use);
               // activity.progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0));
               // activity.textViewProgress.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                if (error) {
                    Toast toast = Toast.makeText(activity.getApplicationContext(), "Ошибка: " + errorMes, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                        result();
                    Toast toast = Toast.makeText(activity.getApplicationContext(), "Готово !", Toast.LENGTH_SHORT);
                    toast.show();
                        activity.finish();
                }
        }

        void result() {
            if (arrayId.size() > 1) {
                Intent Selection = new Intent(Action.this, Selection.class);
                Selection.putExtra("arrayId", arrayId);
                startActivity(Selection);
            } else {
                int idModel = -1;
                if (arrayId.size() == 1) {
                    for (int id : arrayId) {
                        idModel = id;
                    }
                }
                if (idModel != -1) {
                    Intent Information = new Intent(Action.this, Information.class);
                    Information.putExtra("idModel", idModel);
                    startActivity(Information);
                } else {
                    Intent Failure = new Intent(Action.this, Failure.class);
                    startActivity(Failure);
                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.navigation_doPhoto:
                l = System.currentTimeMillis();
                File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera",
                        l + ".jpg");
                Uri outputFileUri = FileProvider.getUriForFile(Action.this, "com.example.homefolder.example.provider", file);
                Intent doPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                doPhoto.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(doPhoto, 2);
                filePath = file.getAbsolutePath();
                break;
            case  R.id.navigation_pickPhoto:
                Intent photoPick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPick, 1);
                break;
            case R.id.navigation_Manual:
                Intent Manual = new Intent(this, Manual.class);
                startActivity(Manual);
                break;
            case R.id.navigation_Exit:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickTextView(View v) {
        if (progress){
                Task.cancel(true);
                progressStop = true;
                textViewUse.setText(R.string.cancelProcess);
                textViewUse.setEnabled(false);
                textViewProgress.setText(R.string.cancelMes);
        } else {
            if (flag) {
                String[] proj = {MediaStore.Images.Media.DATA};
                try (Cursor cursor = this.getContentResolver().query(selectedImage, proj, null, null, null)) {
                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    filePath = cursor.getString(column_index);
                }
            } else {
                filePath = selectedImage.getPath();
            }
            System.out.println("Путь файла " + filePath);

                Task = new MyTask();
                Task.link(this);
                Task.execute();

            System.out.println("Поток " + Task.hashCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            Intent action = new Intent(this, Action.class);
            action.putExtra("selectedImageUri", selectedImage);
            action.putExtra("flag", true);
            startActivity(action);
            this.finish();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            selectedImage = Uri.parse(filePath);
            MediaScannerConnection.scanFile(Action.this,
                    new String[]{filePath}, null, null);
            Intent action = new Intent(this, Action.class);
            action.putExtra("selectedImageUri", selectedImage);
            action.putExtra("flag", false);
            startActivity(action);
            this.finish();
        }
    }

    public int findMatches(String modelimg, int id, Mat descriptors_scene, Mat img_scene, MatOfKeyPoint keypoints_scene)
    {

            Mat img_object = Imgcodecs.imread(Environment.getExternalStorageDirectory() + modelimg);

            Mat grayImageobject = new Mat(img_object.rows(), img_object.cols(), img_object.type());
            Imgproc.cvtColor(img_object, grayImageobject, Imgproc.COLOR_BGRA2GRAY);
            Core.normalize(grayImageobject, grayImageobject, 0, 255, Core.NORM_MINMAX);

            MatOfDMatch matches = new MatOfDMatch();
            LinkedList<DMatch> good_matches = new LinkedList<>();
            MatOfKeyPoint keypoints_object = new MatOfKeyPoint();
            Mat descriptors_object = new Mat();

            FeatureDetector fd = FeatureDetector.create(FeatureDetector.ORB);

            fd.detect(img_object, keypoints_object);

            DescriptorExtractor extractor = DescriptorExtractor.create(3);

            extractor.compute(img_object, keypoints_object, descriptors_object);
            DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

            matcher.match(descriptors_object, descriptors_scene, matches);

            double max_dist = 0;
            double min_dist = 100;
            List<DMatch> matchesList = matches.toList();

            for (int i = 0; i < descriptors_object.rows(); i++) {
                Double dist = (double) matchesList.get(i).distance;
                if (dist < min_dist) min_dist = dist;
                if (dist > max_dist) max_dist = dist;
            }

            for (int i = 0; i < descriptors_object.rows(); i++) {
                if (matchesList.get(i).distance < 3 * min_dist) {
                    good_matches.addLast(matchesList.get(i));
                }
            }

            System.out.println(matchesList.size());
            System.out.println(good_matches.size());

            if (good_matches.size() <= (matchesList.size()*0.4)) {
                return id;
            }
            return -1;
    }
}
