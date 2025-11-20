package com.example.prosty_crud.core.port.in;

import com.example.prosty_crud.core.domain.Car;

public interface ICommandPort {
    Car createCar(Car car);
    Car updateCar(Car car, String id);

    void deleteCar(String id);
}
