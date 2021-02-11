package com.la.model;

import com.la.model.users.Reader;
import com.la.model.enums.TransactionStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class PurchaseBookRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column
    private Double amount;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "purchase_book_request_book",
            joinColumns = @JoinColumn(name = "purchase_book_request_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
    private List<Book> bookList;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;
}
