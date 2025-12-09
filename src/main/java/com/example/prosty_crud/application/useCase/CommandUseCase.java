package com.example.prosty_crud.application.useCase;
import com.example.prosty_crud.core.domain.Car;
import com.example.prosty_crud.core.port.in.ICommandPort;
import com.example.prosty_crud.core.port.out.CarRepositoryCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class CommandUseCase implements ICommandPort {
    private final CarRepositoryCommandPort port;
    @Override
    public Car updateCar(Car car, String id) {
       var updatedCar = port.update(car, id);
       return updatedCar;
    }

    @Override
    public void deleteCar(String id) {
        port.delete(id);
    }

    @Override
    public Car createCar(Car car) {
        var createdCar = port.save(car);
        return createdCar;
    }
}
