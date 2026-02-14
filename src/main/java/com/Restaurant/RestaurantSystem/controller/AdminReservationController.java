package com.Restaurant.RestaurantSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Restaurant.RestaurantSystem.service.ReservationService;

@Controller
public class AdminReservationController {
    
    @Autowired
    private ReservationService reservationService; // 予約関連のサービスを注入

    @GetMapping("/admin/reservations") // /admin/reservationsにGETリクエストが来たとき,管理者用の予約一覧ページを表示する
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations()); // Serviceから全予約を取得し、モデルに追加
        return "admin/reservation-list"; // 管理者用の予約一覧ビューを指定
    }

}
