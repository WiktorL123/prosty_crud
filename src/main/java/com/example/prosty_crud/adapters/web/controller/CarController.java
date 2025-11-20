package com.example.prosty_crud.adapters.web.controller;

import com.example.prosty_crud.adapters.web.dto.CarResponseDto;
import com.example.prosty_crud.adapters.web.dto.CreateCarDto;
import com.example.prosty_crud.adapters.web.dto.UpdateCarDto;
import com.example.prosty_crud.adapters.web.mapper.DtoDomainMapper;
import com.example.prosty_crud.core.port.in.ICommandPort;
import com.example.prosty_crud.core.port.in.IQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.net.URI;

@RestController()
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final ICommandPort port;
    private final IQueryPort queryPort;
    private final DtoDomainMapper mapper;

    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAll(){
        var cars = queryPort.getAll()
                .stream()
                .map(car->mapper.toResponse(car))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(cars);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable String id) {
       var car = mapper.toResponse(queryPort.getById(id));
       return ResponseEntity.status(HttpStatus.OK).body(car);
    }

    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@RequestBody CreateCarDto dto){
       var car = port.createCar(mapper.toDomainCreate(dto));
       var response = mapper.toResponse(car);
       URI location = URI.create("/cars/" + car.id());
       return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCar(@PathVariable String id){
        port.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDto> updateCar(@PathVariable String id, @RequestBody UpdateCarDto car){
        var domainCar = mapper.toDomainUpdate(car);
        var updatedCar = port.updateCar(domainCar, id);
        URI location = URI.create("/cars/" + updatedCar.id());

        return ResponseEntity.
                status(HttpStatus.CREATED)
                        .location(location)
                        .body(mapper
                        .toResponse(updatedCar));

    }



}
