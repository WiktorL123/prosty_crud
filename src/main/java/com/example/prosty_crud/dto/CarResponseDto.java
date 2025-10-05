package com.example.prosty_crud.dto;

public class CarResponseDto {
    public CarResponseDto(String message, CarDto car){
        this.car = car;
        this.message = message;
    }
   private String message;
   private CarDto car;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CarDto getCar() {
        return car;
    }

    public void setCar(CarDto car) {
        this.car = car;
    }
}
