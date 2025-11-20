package com.example.prosty_crud.adapters.postgres.repository;

import com.example.prosty_crud.adapters.postgres.mapper.ModelDomainMapper;
import com.example.prosty_crud.adapters.postgres.model.CarEntity;
import com.example.prosty_crud.core.domain.Car;
import com.example.prosty_crud.core.port.out.CarRepositoryQueryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CarQueryAdapter implements CarRepositoryQueryPort {
    private final CarRepository carRepository;
    private final ModelDomainMapper mapper;

    @Override
    public List<Car> getAll() {
        List<CarEntity> entities = carRepository.findAll();
        return entities
                .stream()
                .map(c->mapper.toDomain(c))
                .toList();
    }

    @Override
    public Car getById(String id) {
        var entity = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not Found"));
        return mapper.toDomain(entity);
    }
}
