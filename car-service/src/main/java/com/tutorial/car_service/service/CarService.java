package com.tutorial.car_service.service;

import com.tutorial.car_service.entity.CarEntity;
import com.tutorial.car_service.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    CarRepository carRepository;

    public List<CarEntity> getAll(){
        return carRepository.findAll();
    }

    public CarEntity getCarById(int id){
        return carRepository.findById(id).orElse(null);
    }

    public CarEntity save(CarEntity car){
        CarEntity newCar = carRepository.save(car);
        System.out.println(car);
        System.out.println(newCar);
        return newCar;
    }

    public List<CarEntity> byUserId(int userId){
        return carRepository.findByUserId(userId);
    }
}
