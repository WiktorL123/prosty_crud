    package com.example.prosty_crud.mapper;

    import com.example.prosty_crud.car.model.Car;
    import com.example.prosty_crud.dto.CarDto;
    import org.springframework.stereotype.Component;

    import java.util.stream.Stream;

    @Component
    public class CarMapper {
        public CarMapper(){}

        public Car mapToCar(CarDto dto){
            Car car = new Car();
            car.setModel(dto.getModel());
            car.setColor(dto.getColor());
            car.setYearOfProduction(dto.getYearOfProduction());
            return car;
        }
        public CarDto mapToCarDto(Car car){
            CarDto dto = new CarDto();
            dto.setColor(car.getColor());
            dto.setModel(car.getModel());
            dto.setYearOfProduction(car.getYearOfProduction());
            return dto;
        }
    }
