package com.tutorial.bike_service.repository;

import com.tutorial.bike_service.entity.BikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeRepository extends JpaRepository<BikeEntity, Integer> {
    List<BikeEntity> findByUserId(int userId);
}
