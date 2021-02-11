package com.la.model;

import com.la.model.enums.TransactionStatus;
import com.la.model.users.Reader;

import javax.persistence.*;

@Entity
public class MembershipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    public MembershipRequest() {
    }

    public MembershipRequest(TransactionStatus status, Reader reader) {
        this.status = status;
        this.reader = reader;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }
}
