package com.example.prosty_crud.car.service;

import com.example.prosty_crud.car.model.Car;
import com.example.prosty_crud.car.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    public CarService( CarRepository carRepository) {
        this.carRepository=carRepository;
    }

    private final CarRepository carRepository;

    public List<Car> getAll() {
        List<Car> cars = carRepository.findAll();
        System.out.println(cars);
        return carRepository.findAll();
    }
    public void createCar(String model, String color, Integer yearOfProduction) {
        Car car = new Car(model, color, yearOfProduction);
        carRepository.save(car);
    }

    public void deleteCar(Long id){
        carRepository.deleteById(id);
    }

    public void updateCar(Long id, Car car) {
        Car existingCar = carRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Car not found"));
        existingCar.setColor(car.getColor());
        existingCar.setModel(car.getModel());
        existingCar.setYearOfProduction(car.getYearOfProduction());

        carRepository.save(existingCar);
    }

    public Car getById(Long id){
        Car car = carRepository.findById(id).orElse(null);
        return car;

    }

}
