package com.example.project.entity;


import javax.persistence.*;
import java.util.List;

@Entity
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;    //название печатной продукции
    private String type;    //тип печатной продукции(книга, журнал, газета и т.д.)
    private String author;  //автор книги(если нет(газеты,журналы), то NULL)

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<PublishEntity> publishes;

    public BookEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<PublishEntity> getPublishes() {
        return publishes;
    }

    public void setPublishes(List<PublishEntity> publishes) {
        this.publishes = publishes;
    }
}
