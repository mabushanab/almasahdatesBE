package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Products {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private float priceForItem;
    private int quantity;
    private float boxCost;
    private int discount;
    private String notes;

    public Products(Item item, float priceForItem, int quantity, float boxCost,int discount, String notes) {
        this.item = item;
        this.priceForItem = priceForItem;
        this.quantity = quantity;
        this.boxCost = boxCost;
        this.discount = discount;
        this.notes = notes;
    }
}
