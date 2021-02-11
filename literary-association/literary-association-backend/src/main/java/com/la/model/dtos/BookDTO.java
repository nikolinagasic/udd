package com.la.model.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookDTO {
    private Long id;
    private String title;
    private String isbn;
    private List<String> keyWords;
    private Integer publishedYear;
    private Integer pagesNumber;
    private String synopsis;
    private Double price;
    private GenreDTO genre;
    private ScriptDTO script;
    private LectorDTO lector;
    private PublisherDTO publisher;
    private WriterDTO writer;
    private EditorDTO editor;
}
