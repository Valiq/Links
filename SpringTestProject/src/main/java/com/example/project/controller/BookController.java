package com.example.project.controller;

import com.example.project.entity.BookEntity;
import com.example.project.exception.BookAlreadyExistException;
import com.example.project.exception.BookNotFoundException;
import com.example.project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public ResponseEntity<String> getView(){        // проверка активности сервера
        try{
            return ResponseEntity.ok("Сервер активен");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body("Сервер не отвечает");
        }
    }

    @GetMapping("/view")
    public ResponseEntity getBooks(){           //получение всей таблицы с печатной продукцией
        try {
            return ResponseEntity.ok(bookService.getBooks());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/byId")        //переменная Id будет взята из поисквой строки в качестве query параметра
    public ResponseEntity getBookById (@RequestParam int id){   //получение по Id печатной продукции и списка всех публикаций
        try {
            return ResponseEntity.ok(bookService.getById(id));
        } catch (BookNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/byName")      //переменная Name будет взята из поисквой строки в качестве query параметра
    public  ResponseEntity getBookByName (@RequestParam String name){ //получение по названию печатной продукции и списка всех публикаций
        try{
            return ResponseEntity.ok(bookService.getByName(name));
        } catch (BookNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/byAuthor")         //переменная author будет взята из поисквой строки в качестве query параметра
    public  ResponseEntity getBookByAuthor(@RequestParam String author){ //получение по автору печатной продукции и списка всех публикаций
        try{
            return ResponseEntity.ok(bookService.getByAuthor(author));
        } catch (BookNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @Cacheable("byType")
    @GetMapping("/byType")       //переменная type будет взята из поисквой строки в качестве query параметра
    public  ResponseEntity getBookByType(@RequestParam String type){ //получение по типу печатной продукции и списка всех публикаций
        try{
            return ResponseEntity.ok(bookService.getByType(type));
        } catch (BookNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> regBook(@RequestBody BookEntity book){    //Добавление нового объекта
        try {
            return ResponseEntity.ok(String.format("Объект успешно сохранен под id = %d",bookService.regBook(book).getId()));
        } catch (BookAlreadyExistException ex) {
            return  ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка при сохранении объекта");
        }
    }


    @PostMapping("/update")         //Обновление объекта по id
    public ResponseEntity<String> updateBook(@RequestParam int id, @RequestBody BookEntity newBook){
        try {
            bookService.updateBook(id,newBook);
            return ResponseEntity.ok(String.format("Объект с id = %d успешно обновлен", id));
        } catch (BookNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка при обновлении объекта");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable int id){     //Удаление объекта
        try {
            bookService.deleteById(id);
            return ResponseEntity.ok(String.format("Объект с id = %d успешно удален", id));
        } catch (BookNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка удаления");
        }
    }

}
