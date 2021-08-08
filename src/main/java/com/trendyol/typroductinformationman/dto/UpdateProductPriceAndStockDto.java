package com.trendyol.typroductinformationman.dto;

import lombok.Data;

@Data
public class UpdateProductPriceAndStockDto {
    private Double oldPrice;
    private Double newPrice;
    private Integer oldStock;
    private Integer newStock;
}
