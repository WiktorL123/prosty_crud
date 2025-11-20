package com.example.prosty_crud.core.port.in;

import com.example.prosty_crud.core.domain.Car;

import java.util.List;

public interface IQueryPort {
    List<Car> getAll();
    Car getById(String id);
}
