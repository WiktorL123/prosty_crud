package com.example.prosty_crud.application.useCase;

import com.example.prosty_crud.core.domain.Car;
import com.example.prosty_crud.core.port.out.CarRepositoryQueryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryUseCaseTest {

    @Mock
    private CarRepositoryQueryPort carRepositoryQueryPort;

    @InjectMocks
    private QueryUseCase queryUseCase;

    @Test
    void getAll_shouldDelegateToRepositoryAndReturnList() {
        // given
        Car car1 = mock(Car.class);
        Car car2 = mock(Car.class);
        List<Car> cars = List.of(car1, car2);

        when(carRepositoryQueryPort.getAll()).thenReturn(cars);

        // when
        List<Car> result = queryUseCase.getAll();

        // then
        assertThat(result).containsExactly(car1, car2);
        verify(carRepositoryQueryPort, times(1)).getAll();
    }

    @Test
    void getById_shouldDelegateToRepositoryAndReturnCar() {
        // given
        String id = "abc";
        Car car = mock(Car.class);

        when(carRepositoryQueryPort.getById(id)).thenReturn(car);

        // when
        Car result = queryUseCase.getById(id);

        // then
        assertThat(result).isSameAs(car);
        verify(carRepositoryQueryPort, times(1)).getById(id);
    }
}
