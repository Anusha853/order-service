package com.q1.order.service;

import com.q1.order.client.InventoryClient;
import com.q1.order.dto.Inventory;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class InventoryService {

    private final InventoryClient inventoryClient;

    public InventoryService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    /*@Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))*/
    @TimeLimiter(name = "inventoryService", fallbackMethod = "fallbackGetInventory")
    public CompletableFuture<Inventory> getInventory(Long productId) {
        System.out.println("Calling Inventory Service for productId: " + productId);
        return CompletableFuture.supplyAsync(() -> inventoryClient.getInventory(productId));
    }

    public CompletableFuture<Inventory> fallbackGetInventory(Long productId, Throwable throwable) {
        System.out.println("Fallback method called for productId: " + productId);
        Inventory inventory = new Inventory(productId.toString(), 0);
        return CompletableFuture.completedFuture(inventory);
    }
}