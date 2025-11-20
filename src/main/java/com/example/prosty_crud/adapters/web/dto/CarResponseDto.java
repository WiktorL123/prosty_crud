package com.example.prosty_crud.adapters.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CarResponseDto {
    private String id;
    private String color;
    private String model;
    private String yearOfProduction;
    private Integer age;
}
