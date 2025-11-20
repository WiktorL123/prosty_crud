package com.example.prosty_crud.adapters.postgres.mapper;

import com.example.prosty_crud.adapters.postgres.model.CarEntity;
import com.example.prosty_crud.core.domain.Car;
import org.springframework.stereotype.Component;

@Component
public class ModelDomainMapper {
    public Car toDomain(CarEntity car){
        String year = String.valueOf(car.getYearOfProduction());
        return new Car(car.getId(), car.getColor(), car.getModel(), year);
    }
    public CarEntity toEntity(Car car){
        Integer year = Integer.valueOf(car.yearOfProduction());
        return new CarEntity(car.model(), car.color(), year);
    }
}
