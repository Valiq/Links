package com.example.project.repository;

import com.example.project.entity.BookEntity;
import com.example.project.model.BookModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepos extends CrudRepository<BookEntity, Integer> {

    public Iterable<BookEntity> findByName(String name);

    public Iterable<BookEntity> findByAuthor(String author);

    public Iterable<BookEntity> findByType(String type);

    public BookEntity findByNameAndAuthorAndType(String name, String author, String type);

   // public Iterable<BookEntity> updateNameById(int id);

  //  public Iterable<BookEntity> updateAuthorById(int id);

  //  public Iterable<BookEntity> updateTypeById(int id);
}
