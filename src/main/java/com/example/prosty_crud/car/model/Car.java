package com.example.prosty_crud.car.model;

import jakarta.persistence.*;

@Entity()
@Table(name = "cars")
public class Car {

    public Car() {}

    public Car( String model, String color, Integer yearOfProduction) {
        this.model =model;
        this.color = color;
        this.yearOfProduction = yearOfProduction;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    private String color;

    private Integer yearOfProduction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(Integer yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }
}
