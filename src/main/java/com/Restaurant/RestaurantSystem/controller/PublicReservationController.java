package com.Restaurant.RestaurantSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.Restaurant.RestaurantSystem.entity.Reservation;
import com.Restaurant.RestaurantSystem.service.ReservationService;

import jakarta.validation.Valid;

@Controller
public class PublicReservationController {
    
    @Autowired
    private ReservationService reservationService; // 予約関連のサービスを注入

    @GetMapping("/reservation") // /reservationにGETリクエストが来たとき,予約ページを表示する
    public String reservationForm(Model model) {
        model.addAttribute("reservation", new Reservation()); // 空のReservationオブジェクトをモデルに追加
        return "public/reservation-form"; // 公開用の予約フォームビューを指定
    }

    @PostMapping("/reservation") // /reservationにPOSTリクエストが来たとき,予約処理を行う
    public String saveReservation(@Valid @ModelAttribute("reservation") Reservation reservation, BindingResult bindingResult) { //@Valid を付けたオブジェクトの すぐ後ろ に BindingResult を置く
        if (bindingResult.hasErrors()) { // バリデーションエラーがある場合
            return "public/reservation-form"; // フォームに戻る
        }
        reservationService.saveReservation(reservation); // Service経由で予約を保存
        return "redirect:/reservation/success"; // 成功ページへリダイレクト
    }

    @GetMapping("/reservation/success") // /reservation/successにGETリクエストが来たとき,予約成功ページを表示する
    public String reservationSuccessPage() {
        return "public/reservation-success"; // 公開用の予約成功ビューを指定
    }
}
