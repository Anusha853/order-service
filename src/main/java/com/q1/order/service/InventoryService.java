package com.q1.order.service;

import com.q1.order.client.InventoryClient;
import com.q1.order.dto.Inventory;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    /*@Bulkhead(name = "inventoryServiceBulkhead", fallbackMethod = "getInventoryFallback")
    public Inventory getInventory(Long productId) {
        System.out.println("Calling Inventory Service for productId: " + productId+ " | Thread: " + Thread.currentThread().getName());
        return inventoryClient.getInventory(productId);
    }

    public Inventory getInventoryFallback(Long productId, Throwable throwable) {
        System.out.println("Bulkhead rejected request for productId: " + productId+ " | Reason: " + throwable.getClass().getSimpleName() + " | Thread: " + Thread.currentThread().getName());
        return new Inventory(productId, 0); // Return a default Inventory object with quantity 0
    }*/

    @Bulkhead(name = "inventoryServiceBulkhead", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "getInventoryFallback")
    public CompletableFuture<Inventory> getInventory(Long productId) {
        System.out.println("Calling Inventory Service for productId: " + productId+ " | Thread: " + Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> inventoryClient.getInventory(productId));
    }

    public CompletableFuture<Inventory> getInventoryFallback(Long productId, Throwable throwable) {
        System.out.println("Bulkhead rejected request for productId: " + productId+ " | Reason: " + throwable.getClass().getSimpleName() + " | Thread: " + Thread.currentThread().getName());
        Inventory inventory = new Inventory(productId, 0); // Return a default Inventory object with quantity 0
        return CompletableFuture.completedFuture(inventory);
    }



}