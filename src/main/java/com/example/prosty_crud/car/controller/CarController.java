package com.example.prosty_crud.car.controller;

import com.example.prosty_crud.car.model.Car;
import com.example.prosty_crud.car.service.CarService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("car")
public class CarController {
    public CarController(CarService carService){
        this.carService=carService;
    }

    private final CarService carService;

    @GetMapping
    public List<Car> getAll(){
        return carService.getAll();
    }

    @PostMapping
    public void createCar(@RequestBody Car car ){
        carService.createCar( car.getColor(), car.getModel(), car.getYearOfProduction());

    }
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id){
        carService.deleteCar(id);
    }
    @PutMapping("/{id}")
    public void updateCar(@PathVariable Long id, @RequestBody Car car){
        carService.updateCar(id, car);
    }
    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getById(id);
    }
}
