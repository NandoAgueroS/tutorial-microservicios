package com.tutorial.user_service.service;

import com.tutorial.user_service.entity.UserEntity;
import com.tutorial.user_service.feignclients.BikeFeignClient;
import com.tutorial.user_service.feignclients.CarFeignClient;
import com.tutorial.user_service.modelos.Bike;
import com.tutorial.user_service.modelos.Car;
import com.tutorial.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CarFeignClient carFeignClient;

    @Autowired
    BikeFeignClient bikeFeignClient;

    public List<UserEntity> getAll(){
        return userRepository.findAll();
    }

    public UserEntity getUserById(int id){
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity save(UserEntity user){
        UserEntity newUser = userRepository.save(user);
        System.out.println(user);
        System.out.println(newUser);
        return newUser;
    }

    public List<Car> getCars(int userId){
        List<Car> cars = restTemplate.getForObject("http://car-service/car/byuser/" + userId, List.class);
        return cars;
    }

    public List<Bike> getBikes(int userId){
        List<Bike> bikes = restTemplate.getForObject("http://bike-service/bike/byuser/" + userId, List.class);
        return bikes;
    }

    public Car saveCar(int userId, Car car){
        //car.setUserId(userId);
        car.setUserId(userId);
        Car carNew = carFeignClient.save(car);
        return carNew;
    }

    public Bike saveBike(int userId, Bike bike){
        //car.setUserId(userId);
        bike.setUserId(userId);
        Bike bikeNew = bikeFeignClient.save(bike);
        return bikeNew;
    }

    public Map<String, Object> getUserAndVehicles(int id){
        Map<String, Object> result = new HashMap<>();
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null){
            result.put("Mensaje", "no existe el usuario");
            return result;}
        result.put("User", user);
        List<Car> cars = carFeignClient.getCars(id);
        if(cars.isEmpty())
            result.put("Cars", "ese user no tiene autos");
        else
            result.put("Cars", cars);
        List<Bike> bikes = bikeFeignClient.getBikes(id);
        if(bikes.isEmpty())
            result.put("Bikes", "ese user no tiene motos");
        else
            result.put("Bikes", bikes);
        return result;
    }
}
