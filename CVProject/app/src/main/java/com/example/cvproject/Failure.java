package com.example.cvproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.File;

public class Failure extends AppCompatActivity {

    Uri selectedImage;
    long l;
    String filePath;
    boolean anima = true;

    public void onResume() {
        super.onResume();
        if (anima) {
            Animation anim;
            anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
            TextView NoResults = findViewById(R.id.NoResults);
            NoResults.startAnimation(anim);
            TextView Fail = findViewById(R.id.Fail);
            Fail.startAnimation(anim);
        }
        anima = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);
    }

    protected void onSaveInstanceState (@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("filePath", filePath);
        outState.putBoolean("anima", anima);
    }

    protected void onRestoreInstanceState(Bundle SavedInstanceState){
        super.onRestoreInstanceState(SavedInstanceState);
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
                Uri outputFileUri = FileProvider.getUriForFile(Failure.this, "com.example.homefolder.example.provider", file);
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
            MediaScannerConnection.scanFile(Failure.this,
                    new String[]{filePath}, null, null);
            Intent action = new Intent(this, Action.class);
            action.putExtra("selectedImageUri", selectedImage);
            action.putExtra("flag", false);
            startActivity(action);
            this.finish();
        }
    }
}
