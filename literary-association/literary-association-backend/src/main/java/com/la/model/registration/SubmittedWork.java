package com.la.model.registration;

import javax.persistence.*;

@Entity
public class SubmittedWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean reviewed;

    @Column
    private String path;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private WriterMembershipRequest writerMembershipRequest;

    public SubmittedWork() {
    }

    public SubmittedWork(boolean reviewed, String path, WriterMembershipRequest writerMembershipRequest) {
        this.reviewed = reviewed;
        this.path = path;
        this.writerMembershipRequest = writerMembershipRequest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public WriterMembershipRequest getWriterMembershipRequest() {
        return writerMembershipRequest;
    }

    public void setWriterMembershipRequest(WriterMembershipRequest writerMembershipRequest) {
        this.writerMembershipRequest = writerMembershipRequest;
    }
}
