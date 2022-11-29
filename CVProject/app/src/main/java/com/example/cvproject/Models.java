package com.example.cvproject;

public class Models {

    private String name;
    private String series;
    private String modelimg;
    private int id;

    Models(int id, String name, String series, String modelimg){

        this.name=name;
        this.series=series;
        this.modelimg=modelimg;
        this.id = id;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getSeries() {
        return this.series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    String getModelimg() {
        return this.modelimg;
    }

    public void setModelimg(String modelimg) {
        this.modelimg = modelimg;
    }
}
