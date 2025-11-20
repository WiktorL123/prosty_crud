package com.example.prosty_crud.adapters.postgres.repository;

import com.example.prosty_crud.adapters.postgres.model.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, String> {


}
