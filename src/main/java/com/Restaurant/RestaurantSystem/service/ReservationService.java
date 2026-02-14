package com.Restaurant.RestaurantSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Restaurant.RestaurantSystem.entity.Reservation;
import com.Restaurant.RestaurantSystem.repository.ReservationRepository;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository; // 予約関連のリポジトリを注入

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll(); // すべての予約を取得
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation); // 予約を保存
    }

    // 必要に応じて削除や更新メソッドを追加
}
