package com.example.project.model;

import com.example.project.entity.BookEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SimplBookModel {
    private int id;
    private String name;    //название печатной продукции
    private String type;    //тип печатной продукции(книга, журнал, газета и т.д.)
    private String author;  //автор книги(если нет(газеты,журналы), то NULL)

    public static BookModel toModel(BookEntity entity){         //конвертер сущности в модель
        BookModel model = new BookModel();

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setType(entity.getType());
        model.setAuthor(entity.getAuthor());

        return model;
    }

    public SimplBookModel() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
