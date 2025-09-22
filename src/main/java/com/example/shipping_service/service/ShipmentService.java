package com.example.shipping_service.service;

import com.example.shipping_service.modal.Shipment;
import com.example.shipping_service.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {
    private final ShipmentRepository repo;

    public ShipmentService(ShipmentRepository repo) {
        this.repo = repo;
    }

    public Shipment createShipment(Shipment shipment) {
        shipment.setCreatedAt(LocalDateTime.now());
        shipment.setUpdatedAt(LocalDateTime.now());
        shipment.setStatus("CREATED");
        return repo.save(shipment);
    }

    public Optional<Shipment> updateStatus(String id, String status) {
        return repo.findById(id).map(existing -> {
            existing.setStatus(status);
            existing.setUpdatedAt(LocalDateTime.now());
            return repo.save(existing);
        });
    }

    public List<Shipment> getByUserId(String userId) {
        return repo.findByUserId(userId);
    }

    public Optional<Shipment> getByTransactionId(String transactionId) {
        return repo.findByTransactionId(transactionId).stream().findFirst();
    }

    public List<Shipment> getAll() {
        return repo.findAll();
    }
}
