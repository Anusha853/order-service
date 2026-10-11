package com.q1.order.service;

import com.q1.order.client.InventoryClient;
import com.q1.order.dto.Inventory;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryClient inventoryClient;

    public InventoryService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    /*@Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))*/
    @RateLimiter(name="inventoryService", fallbackMethod = "fallbackGetInventory")
    public Inventory getInventory(Long productId) {
        System.out.println("Calling Inventory Service for productId: " + productId);
        return inventoryClient.getInventory(productId);
    }

    public Inventory fallbackGetInventory(Long productId, Throwable throwable) {
        System.out.println("Fallback method called for productId: " + productId);
        return new Inventory(productId.toString(), 0);
    }
}