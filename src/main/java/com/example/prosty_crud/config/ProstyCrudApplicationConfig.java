package com.example.prosty_crud.config;

import com.example.prosty_crud.application.useCase.CommandUseCase;
import com.example.prosty_crud.application.useCase.QueryUseCase;
import com.example.prosty_crud.core.port.in.ICommandPort;
import com.example.prosty_crud.core.port.in.IQueryPort;
import com.example.prosty_crud.core.port.out.CarRepositoryCommandPort;
import com.example.prosty_crud.core.port.out.CarRepositoryQueryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProstyCrudApplicationConfig {
    @Bean
    public ICommandPort commandPort(CarRepositoryCommandPort port){
        return new CommandUseCase(port);
    }
    @Bean
    public IQueryPort queryPort (CarRepositoryQueryPort port) {
        return new QueryUseCase(port);
    }
}
