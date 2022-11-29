package com.example.project.model;

import com.example.project.entity.PublishEntity;

import java.time.LocalDate;

public class SimplPublishModel {
    private int id;
    private String publisher;       //название издательства
    private LocalDate date;         // дата издания

    public static SimplPublishModel toModel(PublishEntity entity){
        SimplPublishModel model = new SimplPublishModel();

        model.setId(entity.getId());
        model.setPublisher(entity.getPublisher());
        model.setDate(entity.getDate());

        return model;
    }

    public SimplPublishModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
