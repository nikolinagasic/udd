package com.la.handler;

import java.io.Serializable;

public class BoardMemberOpinion implements Serializable {
    private Long id;
    private String value;

    public BoardMemberOpinion(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BoardMemberOpinion{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
