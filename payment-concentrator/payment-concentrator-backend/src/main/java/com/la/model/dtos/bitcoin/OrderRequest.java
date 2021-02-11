package com.la.model.dtos.bitcoin;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequest {
    private String order_id;
    private Double price_amount;
    private String price_currency;
    private String receive_currency;
    private String title;
    private String description;
    private String callback_url;
    private String cancel_url;
    private String success_url;
    private String token;
}
