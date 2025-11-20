package com.example.prosty_crud.adapters.web.mapper;

import com.example.prosty_crud.adapters.web.dto.CarResponseDto;
import com.example.prosty_crud.adapters.web.dto.CreateCarDto;
import com.example.prosty_crud.adapters.web.dto.UpdateCarDto;
import com.example.prosty_crud.core.domain.Car;
import org.springframework.stereotype.Component;

@Component
public class DtoDomainMapper {
    public Car toDomainCreate(CreateCarDto dto){
        return new Car(null, dto.getColor(), dto.getModel(), dto.getYearOfProduction());
    }
    public Car toDomainUpdate(UpdateCarDto dto){
        return new Car(null, dto.getColor(), dto.getModel(), dto.getYearOfProduction());
    }
    public CarResponseDto toResponse(Car domain){
        return new CarResponseDto(domain.id(), domain.color(), domain.model(), domain.yearOfProduction(), domain.age() );
    }
}
