package com.trendyol.typroductinformationman.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trendyol.typroductinformationman.dto.*;

public interface ProductService {
    ProductDto getProduct(Integer productId);
    String createProduct(CreateProductDto createProductDto);
    String updateProduct(UpdateProductDto updateProductDto) ;
    String updateProductStock(ProductStockDto productStockDto);
    String addCategory(CategoryDto categoryDto);
}

