package com.example.cvproject;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.HashMap;

public class Information extends AppCompatActivity implements View.OnClickListener {

    String filePath;
    int idModel;
    boolean show = false;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    android.database.Cursor Cursor;
    HashMap<String, String> model = new HashMap<>();
    TextView advice,adviceText,download,Show3D,ShowResult;
    LinearLayout.LayoutParams layoutParams;
    Uri ImageUri, contentUri;
    File file;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Intent intent = getIntent();
        idModel = intent.getIntExtra("idModel", -1);
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.open();
        Cursor = db.rawQuery("select " + DatabaseHelper.name + "," + DatabaseHelper.series + "," + DatabaseHelper.specifications + "," + DatabaseHelper.mass + "," + DatabaseHelper.size + "," + DatabaseHelper.description + "," +
                DatabaseHelper.modelimg + "," + DatabaseHelper.modelpdf + "," +  DatabaseHelper.modelmax + "," + DatabaseHelper.id  + " from " + DatabaseHelper.TABLE + " where " + DatabaseHelper.id + " = " + idModel, null);
        Cursor.moveToFirst();
        while (!Cursor.isAfterLast()) {
            model.put("name",Cursor.getString(0));
            model.put("series",Cursor.getString(1));
            model.put("specifications",Cursor.getString(2));
            model.put("mass",Cursor.getString(3));
            model.put("size",Cursor.getString(4));
            model.put("description",Cursor.getString(5));
            model.put("modelimg",Cursor.getString(6));
            model.put("modelpdf",Cursor.getString(7));
            model.put("modelmax",Cursor.getString(8));
            model.put("id",Cursor.getString(9));
            Cursor.moveToNext();
        }
        Cursor.close();
        db.close();

        ImageView imageViewAva = findViewById(R.id.Ava);
        ImageUri = Uri.parse(Environment.getExternalStorageDirectory() + model.get("modelimg"));
        imageViewAva.setImageURI(ImageUri);

        TextView name = findViewById(R.id.name);
        name.setText(model.get("name"));

        TextView series = findViewById(R.id.series);
        series.setText(model.get("series"));

        TextView specifications = findViewById(R.id.specifications);
        specifications.setText(model.get("specifications"));

        TextView mass = findViewById(R.id.mass);
        mass.setText(model.get("mass"));

        TextView size = findViewById(R.id.size);
        size.setText(model.get("size"));

        TextView description = findViewById(R.id.description);
        description.setText(model.get("description"));

        advice = findViewById(R.id.advice);
        advice.setOnClickListener(this);

        adviceText = findViewById(R.id.adviceText);

        download = findViewById(R.id.download);
        download.setOnClickListener(this);

        Show3D = findViewById(R.id.Show3D);
        if (model.get("modelpdf").equals(null)){
            Show3D.setEnabled(false);
            Show3D.setTextColor(getResources().getColor(R.color.notEnabled));
        } else {
            Show3D.setOnClickListener(this);
        }

        ShowResult = findViewById(R.id.Result);
        if (model.get("modelmax").equals(null)) {
            ShowResult.setEnabled(false);
            ShowResult.setTextColor(getResources().getColor(R.color.notEnabled));
        } else {
            ShowResult.setOnClickListener(this);
        }
    }

    protected void onSaveInstanceState (@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("filePath", filePath);
        outState.putInt("idModel",idModel);
    }

    protected void onRestoreInstanceState(Bundle SavedInstanceState){
        super.onRestoreInstanceState(SavedInstanceState);
        filePath = SavedInstanceState.getString("filePath");
        idModel = SavedInstanceState.getInt("idModel");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manual, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.navigation_Exit) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advice:
                if (!show){
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(25, 0, 25, 0);
                    adviceText.setLayoutParams(layoutParams);
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(25, 10, 0, 25);
                    download.setLayoutParams(layoutParams);
                    show = true;
                } else {
                    layoutParams = new LinearLayout.LayoutParams(0, 0);
                    layoutParams.setMargins(0, 0, 0, 0);
                    adviceText.setLayoutParams(layoutParams);
                    layoutParams = new LinearLayout.LayoutParams(0, 0);
                    layoutParams.setMargins(0, 0, 0, 0);
                    download.setLayoutParams(layoutParams);
                    show = false;
                }
                break;
            case R.id.download:
                Intent load = new Intent(Intent.ACTION_VIEW);
                load.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.techsoft3d.hps.pdf.reader"));
                startActivity(load);
                break;
            case R.id.Show3D:
                System.out.println("Путь  " + "content:/" + Environment.getExternalStorageDirectory() + model.get("modelpdf"));
                file = new File(Environment.getExternalStorageDirectory() + model.get("modelpdf"));
                contentUri = FileProvider.getUriForFile(Information.this, "com.example.homefolder.example.provider", file);
                Intent show3D = new Intent(Intent.ACTION_VIEW);
                show3D.setDataAndTypeAndNormalize(contentUri, "application/pdf");
                show3D.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(show3D);
                break;
            case R.id.Result:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("link",model.get("modelmax"));
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(),"Ссылка скопирована в буфер обмена",Toast.LENGTH_SHORT).show();

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.Validrol.RicardoAR");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"Приложение не найдено",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
