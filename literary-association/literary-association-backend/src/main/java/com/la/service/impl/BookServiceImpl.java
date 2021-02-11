package com.la.service.impl;

import com.la.model.Book;
import com.la.model.dtos.BookDTO;
import com.la.model.mappers.BookDTOMapper;
import com.la.repository.BookRepository;
import com.la.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private BookDTOMapper bookDTOMapper;

    @Override
    public List<BookDTO> findAll() {
        List<Book> bookList = bookRepository.findAll();
        List<BookDTO> bookDTOList = new ArrayList<>();
        for (Book book : bookList) {
            bookDTOList.add(bookDTOMapper.toDTO(book));
        }
        System.err.println(bookDTOList.get(0));
        return bookDTOList;
    }

    @Override
    public BookDTO findById(Long id) {
        return bookDTOMapper.toDTO(bookRepository.findById(id).get());
    }
}
