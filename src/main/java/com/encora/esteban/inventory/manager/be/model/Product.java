package com.encora.esteban.inventory.manager.be.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;

    private String name;

    private String category;

    private double unitPrice;

    private LocalDate expirationDate;

    private int quantityInStock;

    private LocalDate creationDate;

    private LocalDate updateDate;
}
