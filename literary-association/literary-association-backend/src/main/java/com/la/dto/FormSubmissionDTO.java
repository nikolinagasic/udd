package com.la.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class FormSubmissionDTO implements Serializable {
    private String fieldId;
    private String fieldValue;
}
