package com.example.prosty_crud.adapters.postgres;

import com.example.prosty_crud.adapters.postgres.mapper.ModelDomainMapper;
import com.example.prosty_crud.adapters.postgres.repository.CarRepository;
import com.example.prosty_crud.core.domain.Car;
import com.example.prosty_crud.core.port.out.CarRepositoryCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarCommandAdapter implements CarRepositoryCommandPort {
    private final CarRepository carRepository;
    private final ModelDomainMapper mapper;
    @Override
    public Car save(Car car) {
        var entity = mapper.toEntity(car);
        var saved  = carRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Car update(Car car, String id) {
        var found = carRepository.findById(id).orElseThrow(()->new RuntimeException("Car not found"));
        found.setColor(car.color());
        found.setModel(car.model());
        found.setYearOfProduction(Integer.valueOf(car.yearOfProduction()));
        var updated  = carRepository.save(found);
        return mapper.toDomain(updated);
    }

    @Override
    public void delete( String id) {
        var found = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
        carRepository.delete(found);

    }
}
