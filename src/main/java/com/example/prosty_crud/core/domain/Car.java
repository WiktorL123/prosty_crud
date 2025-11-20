package com.example.prosty_crud.core.domain;

import java.time.Year;

public record Car(String id, String color, String model, String yearOfProduction ) {
    public Integer age() {
        var year = Integer.valueOf(yearOfProduction);
        return Year.now().getValue() - year;
    }
}
