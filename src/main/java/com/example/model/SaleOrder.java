package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "app_sale_order")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SaleOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sale_order_id") // foreign key in Item table
    private List<Products> products;

    private Date date;
    private float totalPrice;
    private float remainAmount;
    private String descr;

}
