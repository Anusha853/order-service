package com.q1.order.client.config;

import com.q1.order.dto.Inventory;
import feign.RequestTemplate;
import feign.codec.Encoder;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CustomInventoryEncoder implements Encoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) {

        try {
            // Cast to Inventory
            if (object instanceof Inventory inventory) {

                Map<String, Object> data = new HashMap<>();
                data.put("productId", inventory.getProductId());
                data.put("quantity", inventory.getQuantity());

                String json = objectMapper.writeValueAsString(data);

                template.body(json);
                template.header("Content-Type", "application/json");
            }

        } catch (Exception e) {
            throw new RuntimeException("Encoding failed", e);
        }
    }
}