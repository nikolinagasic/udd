package com.la.controller;

import com.la.model.dtos.BookDTO;
import com.la.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/auth/book")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping(value = "")
    public ResponseEntity<List<BookDTO>> findAll() {
        try {
            List<BookDTO> bookList = bookService.findAll();
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
        try {
            BookDTO book = bookService.findById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
