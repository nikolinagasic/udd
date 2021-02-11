package com.la.model;

import com.la.model.users.Editor;
import com.la.model.users.Lector;
import com.la.model.users.Writer;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(unique = true, length = 13)
    private String isbn;

    @ElementCollection
    private List<String> keyWords;

    @Column
    private Integer publishedYear;

    @Column
    private Integer pagesNumber;

    @Lob
    @Column
    private String synopsis;

    @Column
    private Double price;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @OneToOne
    private Script script;

    @ManyToOne
    @JoinColumn(name = "lector_id")
    private Lector lector;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Writer writer;

    @ManyToOne
    @JoinColumn(name = "editor_id")
    private Editor editor;
}
