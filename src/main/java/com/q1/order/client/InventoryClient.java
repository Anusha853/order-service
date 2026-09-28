package com.q1.order.client;

import com.q1.order.client.config.InventoryFeignClientConfig;
import com.q1.order.dto.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service",
        configuration = InventoryFeignClientConfig.class
)
public interface InventoryClient {
    @GetMapping("/inventory/{productId}")
    Inventory getInventory(@PathVariable Long productId);

    @PostMapping("/inventory")
    String updateInventory(@RequestBody Inventory inventory);
}