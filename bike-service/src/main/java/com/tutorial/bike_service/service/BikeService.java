package com.tutorial.bike_service.service;

import com.tutorial.bike_service.entity.BikeEntity;
import com.tutorial.bike_service.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {
    @Autowired
    BikeRepository bikeRepository;

    public List<BikeEntity> getAll(){
        return bikeRepository.findAll();
    }

    public BikeEntity getBikeById(int id){
        return bikeRepository.findById(id).orElse(null);
    }

    public BikeEntity save(BikeEntity bike){
        BikeEntity newBike = bikeRepository.save(bike);
        System.out.println(bike);
        System.out.println(newBike);
        return newBike;
    }

    public List<BikeEntity> byUserId(int userId){
        return bikeRepository.findByUserId(userId);
    }
}
