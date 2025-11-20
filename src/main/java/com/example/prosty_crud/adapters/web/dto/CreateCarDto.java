package com.example.prosty_crud.adapters.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCarDto {
    private  String color;
    private String model;
    private String yearOfProduction;
}
