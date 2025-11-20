package com.example.prosty_crud.core.port.out;

import com.example.prosty_crud.core.domain.Car;

public interface CarRepositoryCommandPort {
    Car save(Car car);
    Car update(Car car, String id);

    void delete(String id);
}
