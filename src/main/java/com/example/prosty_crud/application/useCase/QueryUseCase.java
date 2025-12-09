package com.example.prosty_crud.application.useCase;

import com.example.prosty_crud.core.domain.Car;
import com.example.prosty_crud.core.port.in.IQueryPort;
import com.example.prosty_crud.core.port.out.CarRepositoryQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
public class QueryUseCase implements IQueryPort {

    private final CarRepositoryQueryPort port;

    @Override
    public List<Car> getAll() {
        return port.getAll();
    }

    @Override
    public Car getById(String id) {
        return port.getById(id);
    }
}
