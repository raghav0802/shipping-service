package com.example.shipping_service.repository;

import com.example.shipping_service.modal.Shipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface ShipmentRepository extends MongoRepository<Shipment, String> {

    List<Shipment> findByUserId(String userId);
    List<Shipment> findByStatus(String status);
    List<Shipment> findByTransactionId(String status);

}
