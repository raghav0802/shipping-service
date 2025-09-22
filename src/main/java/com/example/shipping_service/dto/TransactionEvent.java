package com.example.shipping_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private String transactionId;
    private String productId;
    private String userId;
    private int quantity;
    private String type; // IN / OUT / CANCELED
    private LocalDateTime date;
}