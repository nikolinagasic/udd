package com.la.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Email implements Serializable {
    private String emailTo;
    private String emailFrom;
    private String subject;
    private String body;
}
