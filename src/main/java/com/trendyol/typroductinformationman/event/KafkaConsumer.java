package com.trendyol.typroductinformationman.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trendyol.typroductinformationman.dto.CreateProductDto;
import com.trendyol.typroductinformationman.dto.ProductDto;
import com.trendyol.typroductinformationman.dto.UpdateProductDto;
import com.trendyol.typroductinformationman.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private ProductService productService;

    @Autowired
    public KafkaConsumer(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "AddedProductBySupplier", groupId = "trendyol",
            containerFactory = "productKafkaListenerContainerFactory")
    public void consume(@Payload CreateProductDto message) {
        productService.createProduct(message);
    }

    @KafkaListener(topics = "UpdatedProductBySupplier", groupId = "trendyol",
            containerFactory = "updateProductKafkaListenerContainerFactory")
    public void consume(@Payload UpdateProductDto message) {
        productService.updateProduct(message);
    }
}
