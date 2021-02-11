package com.la.model.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String type;
    private String username;
    private String createdAt;
    private String updatedAt;
    private String lastLogin;
    private String active;
}
