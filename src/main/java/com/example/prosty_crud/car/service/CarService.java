package com.example.prosty_crud.car.service;

import com.example.prosty_crud.car.model.Car;
import com.example.prosty_crud.car.repository.CarRepository;
import com.example.prosty_crud.dto.CarDto;
import com.example.prosty_crud.mapper.CarMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {

    public CarService(
            CarRepository carRepository,
            CarMapper mapper
    ) {
        this.carRepository=carRepository;
        this.mapper = mapper;
    }

    private final CarRepository carRepository;
    private final CarMapper mapper;

    public List<CarDto> getAll() {
        //wersja for
//        var dtos = new ArrayList<CarDto>();
            var cars =  carRepository.findAll();
//        for (Car car: cars){
//            CarDto carDto =mapper.mapToCarDto(car);
//            dtos.add(carDto);
//        }
        //wersja OG jsowca
        return cars.stream()
                .map(car -> mapper.mapToCarDto(car))
                .toList();

    }
    public CarDto createCar(CarDto dto) {
       Car car  = mapper.mapToCar(dto);
      Car savedCar =  this.carRepository.save(car);
       return mapper.mapToCarDto(savedCar);
    }

    public void deleteCar(Long id){
        carRepository.deleteById(id);

    }

    public CarDto updateCar(Long id, CarDto car) {

        Car existingCar = carRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Car not found"));
        Car entity = mapper.mapToCar(car);
        existingCar.setColor(entity.getColor());
        existingCar.setModel(entity.getModel());
        existingCar.setYearOfProduction(entity.getYearOfProduction());
        carRepository.save(existingCar);
        return mapper.mapToCarDto(existingCar);
    }

    public CarDto getById(Long id){
        Car car = carRepository.findById(id).orElse(null);
        return mapper.mapToCarDto(car);
    }

}
