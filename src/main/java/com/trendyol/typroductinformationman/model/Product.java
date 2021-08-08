package com.trendyol.typroductinformationman.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double price;
    private String imageURL;
    private String barcode;

    @OneToOne(mappedBy = "product")
    private ProductStock productStock;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category;
}
