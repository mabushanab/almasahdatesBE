package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "app_purchase_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "purchase_order_id") // foreign key in Item table
    private List<Goods> goods;

    private LocalDate date;
    private float totalPrice;
    private float remainAmount;
    private String notes;

//    public PurchaseOrder(Merchant byName, List<Goods> goods, Date date, float totalPrice, float remainAmount, String notes) {
////        this.m
////        this.date = new Date();
////        Date today = new Date();
//
//    }
}
