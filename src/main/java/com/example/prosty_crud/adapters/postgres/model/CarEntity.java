package com.example.prosty_crud.adapters.postgres.model;

import jakarta.persistence.*;
import lombok.*;

@Entity()
@Table(name = "cars")

@Getter
@Setter
@NoArgsConstructor
public class CarEntity {


    public CarEntity(String model, String color, Integer yearOfProduction) {
        this.model = model;
        this.color = color;
        this.yearOfProduction = yearOfProduction;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private String id;

    private String model;

    private String color;

    private Integer yearOfProduction;


}
