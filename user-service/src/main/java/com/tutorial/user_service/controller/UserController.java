package com.tutorial.user_service.controller;

import com.tutorial.user_service.entity.UserEntity;
import com.tutorial.user_service.modelos.Bike;
import com.tutorial.user_service.modelos.Car;
import com.tutorial.user_service.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAll(){
        List<UserEntity> users = userService.getAll();
        if (users.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getById(@PathVariable("id") int id){
        UserEntity user = userService.getUserById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }
    @PostMapping
    public ResponseEntity<UserEntity> save(@RequestBody UserEntity user){
        UserEntity userNew = userService.save(user);
        return ResponseEntity.ok(userNew);
    }

    @CircuitBreaker(name = "carCB", fallbackMethod = "fallBackGetCars")
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable("userId") int userId){
        UserEntity userEntity = userService.getUserById(userId);
        if (userEntity == null)
            return ResponseEntity.notFound().build();
        List<Car> cars = userService.getCars(userId);
        return ResponseEntity.ok(cars);
    }

    @CircuitBreaker(name = "carCB", fallbackMethod = "fallBackSaveCar")
    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId, @RequestBody Car car){
        if (userService.getUserById(userId) == null)
            return ResponseEntity.notFound().build();
        Car carNew = userService.saveCar(userId, car);
        return ResponseEntity.ok(carNew);
    }

    @CircuitBreaker(name = "bikeCB", fallbackMethod = "fallBackGetBikes")
    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable("userId") int userId){
        UserEntity userEntity = userService.getUserById(userId);
        if (userEntity == null)
            return ResponseEntity.notFound().build();
        List<Bike> bikes = userService.getBikes(userId);
        return ResponseEntity.ok(bikes);
    }


    @CircuitBreaker(name = "bikeCB", fallbackMethod = "fallBackSaveBike")
    @PostMapping("/savebike/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike){
        if (userService.getUserById(userId) == null)
            return ResponseEntity.notFound().build();
        Bike bikeNew = userService.saveBike(userId, bike);
        return ResponseEntity.ok(bikeNew);
    }
    @CircuitBreaker(name = "allCB", fallbackMethod = "fallBackGetAll")
    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String, Object>> getAllVehicles(@PathVariable("userId") int userId){
        Map<String, Object> result = userService.getUserAndVehicles(userId);
        return ResponseEntity.ok(result);
    }
    public ResponseEntity<List<Car>> fallBackGetCars(@PathVariable("userId") int userId, RuntimeException e){
        return new ResponseEntity("No se pudieron obtener los autos del usuario " + userId, HttpStatus.OK);
    }
    public ResponseEntity<Car> fallBackSaveCar(@PathVariable("userId") int userId, @RequestBody Car car, RuntimeException e){
        return new ResponseEntity("No se pudo guardar el auto para el usuario " + userId, HttpStatus.OK);
    }
    public ResponseEntity<List<Bike>> fallBackGetBikes(@PathVariable("userId") int userId, RuntimeException e){
        return new ResponseEntity("No se pudieron obtener las motos del usuario " + userId, HttpStatus.OK);
    }
    public ResponseEntity<Bike> fallBackSaveBike(@PathVariable("userId") int userId, @RequestBody Bike bike, RuntimeException e){
        return new ResponseEntity("No se pudo guardar la moto para el usuario " + userId, HttpStatus.OK);
    }
    public ResponseEntity<Map<String, Object>> getAllVehicles(@PathVariable("userId") int userId, RuntimeException e){
        return new ResponseEntity("No se pudieron obtener los vehiculos del usuario " + userId, HttpStatus.OK);
    }
}
