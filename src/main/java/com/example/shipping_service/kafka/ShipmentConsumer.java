package com.example.shipping_service.kafka;

import com.example.shipping_service.dto.TransactionEvent;
import com.example.shipping_service.modal.Shipment;
import com.example.shipping_service.repository.ShipmentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ShipmentConsumer {

    private final ShipmentRepository shipmentRepository;
    private final ObjectMapper objectMapper;

    public ShipmentConsumer(ShipmentRepository shipmentRepository, ObjectMapper objectMapper) {
        this.shipmentRepository = shipmentRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "transactions", groupId = "shipping-service-group")
    public void consume(String message) {
        try {
            // Convert JSON -> TransactionEvent DTO
            TransactionEvent event = objectMapper.readValue(message, TransactionEvent.class);

            // Build Shipment
            Shipment shipment = Shipment.builder()
                    .transactionId(event.getTransactionId())
                    .productId(event.getProductId())
                    .userId(event.getUserId())
                    .quantity(event.getQuantity())
                    .status("CREATED")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            shipmentRepository.save(shipment);

            System.out.println("📦 Shipment created for transaction: " + event.getTransactionId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}