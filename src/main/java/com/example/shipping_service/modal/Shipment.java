package com.example.shipping_service.modal;



import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {
    @Id
    private String id;  // internal DB id

    private String transactionId;
    private String productId;
    private String userId;
    private int quantity;

    private String status; // CREATED, SHIPPED, DELIVERED, CANCELED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

