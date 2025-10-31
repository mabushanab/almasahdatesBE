package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_merchant")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Merchant {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String mobileNumber;
    private String address;
    private String type;
    private int rate;

    private String notes;

}
