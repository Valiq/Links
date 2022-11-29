package com.example.project.service;

import com.example.project.entity.BookEntity;
import com.example.project.exception.BookAlreadyExistException;
import com.example.project.exception.BookNotFoundException;
import com.example.project.model.BookModel;
import com.example.project.repository.BookRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepos bookRepos;

    public BookEntity regBook(BookEntity book) throws BookAlreadyExistException {     //Добавление новой печатной продукции
        if (bookRepos.findByNameAndAuthorAndType(book.getName(), book.getAuthor(), book.getType()) != null){
            throw new BookAlreadyExistException("Объект с такими данными уже был создан");
        }
        return bookRepos.save(book);
    }

    public Iterable<BookModel> getBooks() {         //получение списка со всей печатной продукцией и её изданиями
        Iterable<BookEntity> book = bookRepos.findAll();
        return EntitiesToModels(book);
    }

    @Cacheable(cacheNames = {"recordsCache"}, key = "#id")
    public BookModel getById(int id) throws BookNotFoundException {
        if (bookRepos.findById(id).isEmpty()){
            throw new BookNotFoundException(String.format("Объект с id = %d не найден", id));
        }
        BookEntity book = bookRepos.findById(id).get();
        return BookModel.toModel(book);
    }

    @Cacheable({"recordsCache"})
    public Iterable<BookModel> getByName(String name) throws BookNotFoundException {   //получение печатной продукции по имени
        Iterable<BookEntity> book = bookRepos.findByName(name);
        if (book.spliterator().getExactSizeIfKnown() == 0)  {
            throw new BookNotFoundException(String.format("Объект с именем: \"%s\" не найден",name));
        }
        return EntitiesToModels(book);
    }

    @Cacheable({"recordsCache"})
    public Iterable<BookModel> getByAuthor(String author) throws BookNotFoundException {       //получение печатной продукции по автору
        Iterable<BookEntity> book = bookRepos.findByAuthor(author);
        if (book.spliterator().getExactSizeIfKnown() == 0)  {
            throw new BookNotFoundException(String.format("Объект с автором: \"%s\" не найден",author));
        }
        return EntitiesToModels(book);
    }

    @Cacheable({"recordsCache"})
    public Iterable<BookModel> getByType(String type) throws BookNotFoundException {       //получение печатной продукции по типу
        Iterable<BookEntity> book = bookRepos.findByType(type);
        if (book.spliterator().getExactSizeIfKnown() == 0)  {
            throw new BookNotFoundException(String.format("Объект с типом печатной продукции: \"%s\" не найден",type));
        }
        //book.forEach(BookModel::toModel);
        return EntitiesToModels(book);
    }

    @CachePut(cacheNames = {"recordsCache"}, key = "#id")
    public void updateBook (int id, BookEntity newBook) throws BookNotFoundException {     //обновление строки по id из таблицы печатной продукции
        if (bookRepos.findById(id).isEmpty()){
            throw new BookNotFoundException(String.format("Объект с данным id = %d не существует",id));
        }
        BookEntity book = bookRepos.findById(id).get();

        if (newBook.getName() != null)
            book.setName(newBook.getName());

        if (newBook.getAuthor() != null)
            book.setAuthor(newBook.getAuthor() );

        if (newBook.getType() != null)
            book.setType(newBook.getType());

        bookRepos.save(book);
    }

    @CacheEvict(cacheNames = {"recordsCache"}, key = "#id")
    public void deleteById(int id) throws BookNotFoundException {       //удаление по id строки из таблицы печатной продукции
        if (bookRepos.findById(id).isEmpty()) {
            throw new BookNotFoundException(String.format("Объект с данным id = %d не существует",id));
        }
        bookRepos.deleteById(id);
    }

    public static List<BookModel> EntitiesToModels(Iterable<BookEntity> book){   //конвертер коллекции entity в model
        List<BookModel> model = new ArrayList<>();
        for (var entity:book) {
            model.add(BookModel.toModel(entity));
        }
        return model;
    }
}
