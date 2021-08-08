package com.trendyol.typroductinformationman.dto;

import lombok.Data;


@Data
public class UpdateProductDto {
    private Long productId;
    private String title;
    private Double price;
    private String imageURL;
    private String barcode;
    private Integer stock;
    private Integer categoryId;
}
