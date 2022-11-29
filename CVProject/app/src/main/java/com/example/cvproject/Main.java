package com.example.cvproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.opencv.android.OpenCVLoader;

import java.io.File;

public class Main extends AppCompatActivity implements View.OnClickListener {
    static {
        System.loadLibrary("opencv_java3");
    }

    long l ;
    String filePath;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug()) {
            Log.e("Error", "Cannot load OpenCV library"); }
            else {System.out.println("Good");}
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                System.out.println("Пользователь разрешил доступ");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.create_db();

        LinearLayout LinearDoPhoto = findViewById(R.id.LinearDoPhoto);
        LinearDoPhoto.setOnClickListener(this);

        LinearLayout LinearGallery = findViewById(R.id.LinearGallery);
        LinearGallery.setOnClickListener(this);

        LinearLayout LinearLibrary = findViewById(R.id.LinearLibrary);
        LinearLibrary.setOnClickListener(this);

        LinearLayout LinearBell = findViewById(R.id.LinearBell);
        LinearBell.setOnClickListener(this);

        LinearLayout LinearExit = findViewById(R.id.LinearExit);
        LinearExit.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Пользователь разрешил доступ");
            } else {
                System.out.println("Пользователь запретил доступ");
            }
        }
    }

    protected void onSaveInstanceState (@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("filePath", filePath);
    }

    protected void onRestoreInstanceState(Bundle SavedInstanceState){
        super.onRestoreInstanceState(SavedInstanceState);
        filePath = SavedInstanceState.getString("filePath");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LinearDoPhoto:
                l = System.currentTimeMillis();
                File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera",
                        l + ".jpg");
                Uri outputFileUri = FileProvider.getUriForFile(Main.this, "com.example.homefolder.example.provider", file);
                System.out.println(file.getAbsolutePath());
                Intent doPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                doPhoto.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(doPhoto, 2);
                filePath = file.getAbsolutePath();
                break;
            case R.id.LinearGallery:
                Intent photoPick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPick, 1);
                break;
            case R.id.LinearLibrary:
                Intent library = new Intent(this, Library.class);
                startActivity(library);
                break;
            case R.id.LinearBell:
                Intent Manual = new Intent(this, Manual.class);
                startActivity(Manual);
                break;
            case R.id.LinearExit:
                System.exit(0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Intent action = new Intent(this, Action.class);
            action.putExtra("selectedImageUri", selectedImage);
            action.putExtra("flag", true);
            startActivity(action);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri selectedImageUri = Uri.parse(filePath);
            MediaScannerConnection.scanFile(Main.this,
                    new String[]{filePath}, null, null);
            Intent action = new Intent(this, Action.class);
            action.putExtra("selectedImageUri", selectedImageUri);
            action.putExtra("flag", false);
            startActivity(action);
        }
    }
}
