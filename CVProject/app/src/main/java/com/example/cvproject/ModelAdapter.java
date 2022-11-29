package com.example.cvproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ModelAdapter extends ArrayAdapter<Models> {

    private LayoutInflater inflater;
    private int layout;
    private List<Models> models;

    ModelAdapter(Context context, int resource, List<Models> models) {

        super(context, resource, models);
        this.models = models;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

       @SuppressLint("ViewHolder") View view = inflater.inflate(this.layout, parent, false);

        ImageView ImgModelView = view.findViewById(R.id.img);
        TextView nameView = view.findViewById(R.id.name);
        TextView seriesView = view.findViewById(R.id.series);

        Models state = models.get(position);

        Uri ImageUri = Uri.parse(Environment.getExternalStorageDirectory() + state.getModelimg());
        ImgModelView.setImageURI(ImageUri);
        nameView.setText(state.getName());
        seriesView.setText(state.getSeries());

        return view;
    }
}
