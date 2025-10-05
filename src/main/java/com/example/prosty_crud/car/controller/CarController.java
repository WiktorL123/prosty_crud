package com.example.prosty_crud.car.controller;

import com.example.prosty_crud.car.model.Car;
import com.example.prosty_crud.car.service.CarService;
import com.example.prosty_crud.dto.CarDto;
import com.example.prosty_crud.dto.CarResponseDto;
import jakarta.servlet.ServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.smartcardio.Card;
import java.util.List;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestController()
@RequestMapping("car")
public class CarController {
    public CarController(CarService carService){
        this.carService=carService;
    }

    private final CarService carService;

    @GetMapping
    public ResponseEntity<List<CarDto> >getAll(){
        List<CarDto> cars = carService.getAll();
        return ResponseEntity.ok(cars);
    }

    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@RequestBody CarDto car ){
        var created = carService.createCar(car);
        var response = new CarResponseDto("Created", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCar(@PathVariable Long id){
        carService.deleteCar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long id, @RequestBody CarDto car){
       CarDto updatedCar =  carService.updateCar(id, car);
       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(updatedCar);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long id) {
        CarDto car = carService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(car);
    }
}
