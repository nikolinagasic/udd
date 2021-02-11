package com.la.model.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String type;
    private String firstName;
    private String lastName;
    private String state;
    private String city;
    private String email;
}
