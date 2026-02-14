package com.Restaurant.RestaurantSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Restaurant.RestaurantSystem.entity.Reservation;

//ReservationのCRUD操作のためのインターフェース。
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
}
