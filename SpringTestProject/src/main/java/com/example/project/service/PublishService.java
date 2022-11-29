package com.example.project.service;

import com.example.project.entity.BookEntity;
import com.example.project.entity.PublishEntity;
import com.example.project.exception.BookNotFoundException;
import com.example.project.exception.PublishAlreadyExistException;
import com.example.project.exception.PublishNotFoundException;
import com.example.project.model.PublishModel;
import com.example.project.repository.BookRepos;
import com.example.project.repository.PublishRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PublishService {
    @Autowired
    private PublishRepos publishRepos;
    @Autowired
    private BookRepos bookRepos;

    public PublishEntity createPublish (PublishEntity publish, int bookId) throws PublishAlreadyExistException {     //добавление изданий печатной продукции
        BookEntity book = bookRepos.findById(bookId).get();
        System.out.println(book);
        System.out.println(publishRepos.findByPublisherAndDateAndBook(publish.getPublisher(),publish.getDate(), book));
        if (publishRepos.findByPublisherAndDateAndBook(publish.getPublisher(),publish.getDate(), book) != null){
            throw new PublishAlreadyExistException("Запись с указанной публикацией уже существует");
        }
        publish.setBook(book);
        return publishRepos.save(publish);
    }

    public Iterable<PublishModel> getPublishes() {         //получение списка со всеми публикациями
        Iterable<PublishEntity> publish =  publishRepos.findAll();
        return EntitiesToModels(publish);
    }

    @Cacheable(cacheNames = {"recordsCache"}, key = "#id")
    public PublishModel getById (int id) throws PublishNotFoundException {     //получение изданий по id
        if (publishRepos.findById(id).isEmpty()){
            throw new PublishNotFoundException(String.format("Объект с id = %d не найден", id));
        }
        PublishEntity publish = publishRepos.findById(id).get();
        return PublishModel.toModel(publish);
    }

    @Cacheable({"recordsCache"})
    public Iterable<PublishModel> getByPublisher (String publisher) throws PublishNotFoundException {      //получение изданий по издательству
        Iterable<PublishEntity> publish = publishRepos.findByPublisher(publisher);
        if (publish.spliterator().getExactSizeIfKnown() == 0){
            throw new PublishNotFoundException(String.format("Издательство с именем: \"%s\" не найден",publisher));
        }
        return EntitiesToModels(publish);
    }

    @Cacheable({"recordsCache"})
    public List<PublishModel> getByDate (LocalDate date) throws PublishNotFoundException {      //получение изданий по дате издания
        Iterable<PublishEntity> publish = publishRepos.findByDate(date);
        if (publish.spliterator().getExactSizeIfKnown() == 0){
            throw new PublishNotFoundException(String.format("Издательство с датой: %s не найдено",DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date)));
        }
        return EntitiesToModels(publish);
    }

    @CachePut(cacheNames = {"recordsCache"}, key = "#id")
    public void updatePublish (int id, PublishEntity newPublish) throws PublishNotFoundException, BookNotFoundException {    //обновление строки по id из таблицы изданий печатной продукции
       if (publishRepos.findById(id).isEmpty()){
           throw new PublishNotFoundException(String.format("Ошибка: Издание с id = %d не найдено",id));
       }
       PublishEntity publish = publishRepos.findById(id).get();

       if (newPublish.getPublisher() != null)
            publish.setPublisher(newPublish.getPublisher());

        if (newPublish.getDate() != null)
            publish.setDate(newPublish.getDate());

       if (newPublish.getBook() != null) {
           if (bookRepos.findById(newPublish.getBook().getId()).isEmpty()) {
               throw new BookNotFoundException(String.format("Ошибка: Печатная продукция с id = %d не найдена", id));
           }
           publish.setBook(newPublish.getBook());
       }
        publishRepos.save(publish);
    }

    @CacheEvict(cacheNames = {"recordsCache"}, key = "#id")
    public void deleteById(int id) throws PublishNotFoundException {
        if (publishRepos.findById(id).isEmpty()){
            throw new PublishNotFoundException(String.format("Объект с данным id = %d не существует",id));
        }
        publishRepos.deleteById(id);
    }

    public static List<PublishModel> EntitiesToModels(Iterable<PublishEntity> publish){   //конвертер коллекции entity в model
        List<PublishModel> model = new ArrayList<>();
        for (var entity:publish) {
            model.add(PublishModel.toModel(entity));
        }
        return model;
    }
}
