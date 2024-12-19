package com.tutorial.user_service.modelos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private String brand;
    private String model;
    private int userId;
}
