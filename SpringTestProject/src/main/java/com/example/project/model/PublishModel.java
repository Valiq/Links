package com.example.project.model;

import com.example.project.entity.PublishEntity;

import java.time.LocalDate;

public class PublishModel {
    private int id;
    private String publisher;       //название издательства
    private LocalDate date;         // дата издания
    private SimplBookModel book;        // печатная продукция

    public static PublishModel toModel(PublishEntity entity){        //конвертер сущности в модель
        PublishModel model = new PublishModel();

        model.setId(entity.getId());
        model.setPublisher(entity.getPublisher());
        model.setDate(entity.getDate());

        SimplBookModel bookModel = new SimplBookModel();

        bookModel.setId(entity.getBook().getId());
        bookModel.setName(entity.getBook().getName());
        bookModel.setAuthor(entity.getBook().getAuthor());
        bookModel.setType(entity.getBook().getType());

        model.setBook(bookModel);
        return model;
    }


    public PublishModel() {
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

    public SimplBookModel getBook() {
        return book;
    }

    public void setBook(SimplBookModel book) {
        this.book = book;
    }
}
