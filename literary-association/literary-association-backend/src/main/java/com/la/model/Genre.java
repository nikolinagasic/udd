package com.la.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String value;

    public Genre(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
