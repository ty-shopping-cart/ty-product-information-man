package com.trendyol.typroductinformationman.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.trendyol.typroductinformationman.dto.CategoryDto;
import com.trendyol.typroductinformationman.dto.CreateProductDto;
import com.trendyol.typroductinformationman.dto.ProductDto;
import com.trendyol.typroductinformationman.dto.UpdateProductDto;
import com.trendyol.typroductinformationman.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<ProductDto> getProduct (Integer productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PostMapping("/")
    public ResponseEntity<String> createProduct (@RequestBody CreateProductDto createProductDto) {
        return ResponseEntity.ok(productService.createProduct(createProductDto));
    }

    @PutMapping("/")
    public ResponseEntity<String> updateProduct (@RequestBody UpdateProductDto updateProductDto) {
        return ResponseEntity.ok(productService.updateProduct(updateProductDto));
    }

    @PostMapping("/createCategory")
    public ResponseEntity<String> createCategory (@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(productService.addCategory(categoryDto));
    }


}
