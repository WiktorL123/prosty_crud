package com.example.prosty_crud.dto;

public class CarDto {
    public CarDto(){}
    public CarDto(String model, String color, Integer yearOfProduction) {}
    private String model;
    private String color;
    private Integer yearOfProduction;

    public Integer getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(Integer yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
