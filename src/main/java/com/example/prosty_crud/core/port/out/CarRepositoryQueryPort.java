package com.example.prosty_crud.core.port.out;

import com.example.prosty_crud.core.domain.Car;

import java.util.List;

public interface CarRepositoryQueryPort {
    List<Car> getAll();
    Car getById(String id);
}
