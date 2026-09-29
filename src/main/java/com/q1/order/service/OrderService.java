package com.q1.order.service;

import com.netflix.appinfo.InstanceInfo;
import com.q1.order.client.InventoryClient;
import com.q1.order.dto.Inventory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class OrderService {
    private final InventoryClient inventoryClient;
    private  final RestTemplate restTemplate;
    private final RestClient restClient;
    private final DiscoveryClient discoveryClient;
    private final InventoryService inventoryService;

    public OrderService(InventoryClient inventoryClient, RestTemplate restTemplate, RestClient restClient, DiscoveryClient discoveryClient, InventoryService inventoryService) {
        this.inventoryClient = inventoryClient;
        this.restTemplate = restTemplate;
        this.restClient = restClient;
        this.discoveryClient = discoveryClient;
        this.inventoryService = inventoryService;
    }

    public String placeOrder(Long productId){

        Inventory inventory = inventoryService.getInventory(productId);
        int quantity = inventory.getQuantity();
        if(quantity <=0)
            return "Product Out Of Stock";
        updateInventory(inventory);

        return "Order Placed Successfully";
    }

    private void updateInventory(Inventory inventory) {
        inventory.setQuantity(inventory.getQuantity()-1);
        inventoryClient.updateInventory(inventory);

    }


}