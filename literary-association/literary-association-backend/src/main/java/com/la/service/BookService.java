package com.la.service;

import com.la.model.Book;
import com.la.model.dtos.BookDTO;

import java.util.List;

public interface BookService {

    List<BookDTO> findAll();

    BookDTO findById(Long id);
}
