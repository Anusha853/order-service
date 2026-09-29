package com.q1.order.service;

import com.q1.order.client.InventoryClient;
import com.q1.order.dto.Inventory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryClient inventoryClient;

    public InventoryService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @CircuitBreaker(name = "inventoryServiceCircuitBreaker", fallbackMethod = "getInventoryFallback")
    public Inventory getInventory(Long productId) {
        System.out.println("Calling Inventory Service for productId: " + productId);
        return inventoryClient.getInventory(productId);
    }

    public Inventory getInventoryFallback(Long productId, Throwable throwable) {
        System.out.println("Inventory Service is down. Returning fallback response for productId: " + productId);
        return new Inventory(productId, 0); // Return a default Inventory object with quantity 0
    }

}