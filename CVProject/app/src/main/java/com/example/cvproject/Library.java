package com.example.cvproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Library extends AppCompatActivity {

    SQLiteDatabase db;
    DatabaseHelper sqlHelper;
    android.database.Cursor Cursor;
    ArrayList<Models> modelsList = new ArrayList<>();
    ListView List;
    EditText Search;
    ModelAdapter modelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        sqlHelper = new DatabaseHelper(this);
        db =  sqlHelper.open();
            Cursor = db.rawQuery("select " + DatabaseHelper.id + "," + DatabaseHelper.name + "," + DatabaseHelper.series + "," +
                    DatabaseHelper.modelimg + " from " + DatabaseHelper.TABLE, null);
            Cursor.moveToFirst();
            while (!Cursor.isAfterLast()) {
                modelsList.add(new Models(Cursor.getInt(0),Cursor.getString(1),
                        Cursor.getString(2),Cursor.getString(3)));
                Cursor.moveToNext();
            }
        Cursor.close();
        db.close();

        List = findViewById(R.id.ListLib);
        Search = findViewById(R.id.search);
        modelAdapter = new ModelAdapter(this, R.xml.list_item, modelsList);
        List.setAdapter(modelAdapter);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Models selectedState = (Models)parent.getItemAtPosition(position);
                int idModel = selectedState.getId();
                Intent Information = new Intent(Library.this, Information.class);
                Information.putExtra("idModel", idModel);
                startActivity(Information);
            }
        };
        List.setOnItemClickListener(itemListener);
        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Search.getText().toString().equals("")){
                    List.setAdapter(modelAdapter);
                } else {
                    ArrayList<Models> SearchModelsList = new ArrayList<>();
                    for (Models mod : modelsList) {
                        if (((mod.getName() + " " + mod.getSeries()).toLowerCase()).contains(Search.getText().toString().toLowerCase())) {
                            SearchModelsList.add(mod);
                        }
                    }
                    ModelAdapter SearchModelAdapter = new ModelAdapter(Library.this, R.xml.list_item, SearchModelsList);
                    List.setAdapter(SearchModelAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
}
