package com.example.project.controller;

import com.example.project.entity.PublishEntity;
import com.example.project.exception.BookNotFoundException;
import com.example.project.exception.PublishAlreadyExistException;
import com.example.project.exception.PublishNotFoundException;
import com.example.project.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/publish")
public class PublishController {
    @Autowired
    private PublishService publishService;

    @GetMapping("/view")
    public ResponseEntity getPublishes(){           //получение всей таблицы с печатной продукцией
        try {
            return ResponseEntity.ok(publishService.getPublishes());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/byId")        //поиск публикаций по id
    public ResponseEntity getPublishById(@RequestParam int id){
        try {
            return ResponseEntity.ok(publishService.getById(id));
        } catch (PublishNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/byPublisher")         //поиск публикаций по наззванию издательства
    public ResponseEntity getPublishByPublisher(@RequestParam String publisher){
        try {
            return ResponseEntity.ok(publishService.getByPublisher(publisher));
        } catch (PublishNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/byDate")              //поиск публикаций по дате
    public ResponseEntity getPublishByDate(@RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate date){
        try {
            return  ResponseEntity.ok(publishService.getByDate(date));
        } catch (PublishNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PostMapping("/add")                        //Добавление нового издания(с указанием id печатной продукции)
    public ResponseEntity createPublish (@RequestBody PublishEntity publish, @RequestParam int id){
        try {
            return ResponseEntity.ok(String.format("Объект успешно сохранён под id = %d", publishService.createPublish(publish,id).getId()));
        } catch (PublishAlreadyExistException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

   @PostMapping("/update")      //обновление строки по id в таблице публикаций
   public ResponseEntity<String> updatePublish(@RequestParam int id, @RequestBody PublishEntity newPublish){
        try{
            publishService.updatePublish(id,newPublish);
            return ResponseEntity.ok(String.format("Объект с id = %d успешно обновлен", id));
        } catch (PublishNotFoundException | BookNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
   }

   @DeleteMapping("{id}")       //удаление изданий по id
    public ResponseEntity<String> deletePublishById(@PathVariable int id){
       try {
            publishService.deleteById(id);
           return ResponseEntity.ok(String.format("Объект с id = %d успешно удален", id));
       } catch (PublishNotFoundException ex) {
           return ResponseEntity.badRequest().body(ex.getMessage());
       } catch (Exception ex) {
           return ResponseEntity.badRequest().body("Произошла ошибка удаления");
       }
   }
}
