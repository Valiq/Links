package com.example.project.repository;

import com.example.project.entity.BookEntity;
import com.example.project.entity.PublishEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface PublishRepos extends CrudRepository<PublishEntity, Integer> {

    public Iterable<PublishEntity> findByPublisher(String publisher);

    public Iterable<PublishEntity> findByDate (LocalDate date);

    public PublishEntity findByPublisherAndDateAndBook (String publisher, LocalDate date, BookEntity book);
}
