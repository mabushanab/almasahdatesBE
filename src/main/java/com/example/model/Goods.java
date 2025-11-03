package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_goods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goods {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    private float priceForGrams;
    private float weightInGrams;
    private String notes;
    private int discount;

    public Goods(Item item, float priceForGrams, float weightInGrams,int discount, String notes) {
        this.item = item;
        this.priceForGrams = priceForGrams;
        this.weightInGrams = weightInGrams;
        this.discount = discount;
        this.notes = notes;
    }
}
