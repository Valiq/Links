package com.example.cvproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;


public class Selection extends AppCompatActivity {

    Uri selectedImage;
    long l;
    String filePath;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    android.database.Cursor Cursor;
    ArrayList<Integer> arrayId = new ArrayList<>();
    ArrayList<Models> modelsList = new ArrayList<>();
    ListView List;
    boolean anima = true;

    public void onResume() {
        super.onResume();
        if (anima) {
            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_bottom_to_top_slide);
            List.setLayoutAnimation(controller);
        }
        anima = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        Intent action = getIntent();
        arrayId = action.getIntegerArrayListExtra("arrayId");
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.open();
        for (int id : arrayId) {
            Cursor = db.rawQuery("select " + DatabaseHelper.id + "," + DatabaseHelper.name + "," + DatabaseHelper.series + "," +
                    DatabaseHelper.modelimg + " from " + DatabaseHelper.TABLE + " where " + DatabaseHelper.id + " = " + id, null);
            Cursor.moveToFirst();
            while (!Cursor.isAfterLast()) {
                modelsList.add(new Models(Cursor.getInt(0),Cursor.getString(1),
                        Cursor.getString(2),Cursor.getString(3)));
                Cursor.moveToNext();
            }
        }
        Cursor.close();
        db.close();
        List = findViewById(R.id.List);
        ModelAdapter modelAdapter = new ModelAdapter(this, R.xml.list_item, modelsList);
        List.setAdapter(modelAdapter);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Models selectedState = (Models)parent.getItemAtPosition(position);
                int idModel = selectedState.getId();
                Intent Information = new Intent(Selection.this, Information.class);
                Information.putExtra("idModel", idModel);
                startActivity(Information);
            }
        };
        List.setOnItemClickListener(itemListener);
    }

    protected void onSaveInstanceState (@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("filePath", filePath);
        outState.putIntegerArrayList("arrayId",arrayId);
        outState.putBoolean("anima", anima);
    }

    protected void onRestoreInstanceState(Bundle SavedInstanceState){
        super.onRestoreInstanceState(SavedInstanceState);
        arrayId = SavedInstanceState.getIntegerArrayList("arrayId");
        filePath = SavedInstanceState.getString("filePath");
        anima = SavedInstanceState.getBoolean("anima");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.navigation_doPhoto:
                l = System.currentTimeMillis();
                File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera",
                        l + ".jpg");
                Uri outputFileUri = FileProvider.getUriForFile(Selection.this, "com.example.homefolder.example.provider", file);
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
            MediaScannerConnection.scanFile(Selection.this,
                    new String[]{filePath}, null, null);
            Intent action = new Intent(this, Action.class);
            action.putExtra("selectedImageUri", selectedImage);
            action.putExtra("flag", false);
            startActivity(action);
            this.finish();
        }
    }
}
