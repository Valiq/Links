package com.example.project.entity;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class PublishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String publisher;        //название издательства
    private LocalDate date;     //дата издания

    @ManyToOne
    @JoinColumn(name = "book")
    private BookEntity book;        //печатная продукция(связь происходит по id)

    public PublishEntity() {
    }

    public int getId() {
        return id;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getDate() {
        return date;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPublisher(String name) {
        this.publisher = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }
}
