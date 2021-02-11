package com.la.model.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseBookRequestDTO {
    private Double amount;
    private List<Long> bookList;
}
