package com.example.prosty_crud.application.useCase;

import com.example.prosty_crud.core.domain.Car;
import com.example.prosty_crud.core.port.out.CarRepositoryCommandPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandUseCaseTest {

    @Mock
    private CarRepositoryCommandPort carRepositoryCommandPort;

    @InjectMocks
    private CommandUseCase commandUseCase;

    @Test
    void createCar_shouldDelegateToRepositoryAndReturnCreatedCar() {
        // given
        Car inputCar = mock(Car.class);
        Car savedCar = mock(Car.class);

        when(carRepositoryCommandPort.save(inputCar)).thenReturn(savedCar);

        // when
        Car result = commandUseCase.createCar(inputCar);

        // then
        assertThat(result).isSameAs(savedCar);
        verify(carRepositoryCommandPort, times(1)).save(inputCar);
    }

    @Test
    void updateCar_shouldDelegateToRepositoryAndReturnUpdatedCar() {
        // given
        String id = "123";
        Car carToUpdate = mock(Car.class);
        Car updatedCar = mock(Car.class);

        when(carRepositoryCommandPort.update(carToUpdate, id)).thenReturn(updatedCar);

        // when
        Car result = commandUseCase.updateCar(carToUpdate, id);

        // then
        assertThat(result).isSameAs(updatedCar);
        verify(carRepositoryCommandPort, times(1)).update(carToUpdate, id);
    }

    @Test
    void deleteCar_shouldDelegateToRepository() {
        // given
        String id = "123";

        // when
        commandUseCase.deleteCar(id);

        // then
        verify(carRepositoryCommandPort, times(1)).delete(id);
        verifyNoMoreInteractions(carRepositoryCommandPort);
    }
}
