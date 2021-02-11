package com.la.model.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PublisherDTO {
    private Long id;
    private String name;
    private String city;
    private String state;
}
