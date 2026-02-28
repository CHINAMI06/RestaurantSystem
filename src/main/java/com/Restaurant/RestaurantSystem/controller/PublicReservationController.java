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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PublicReservationController {
    
    @Autowired
    private ReservationService reservationService; // 予約関連のサービスを注入

    private static final Logger logger = LoggerFactory.getLogger(PublicReservationController.class);

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
        // 予約を保存してから、非同期でPythonスクリプトを起動して確認メールを送信する
        Reservation saved = reservationService.saveReservation(reservation); // Service経由で予約を保存

        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String datetime = saved.getReservationDateTime().format(fmt);
            List<String> cmd = Arrays.asList(
                "python",
                "scripts/send_reservation_email.py",
                "--to", saved.getContact(),
                "--name", saved.getName(),
                "--datetime", datetime,
                "--people", String.valueOf(saved.getNumberOfPeople())
            );
            logger.info("Starting email sender: {}", String.join(" ", cmd));
            Process p = new ProcessBuilder(cmd).start();
            logger.info("Email process started: {}", p);
        } catch (Exception e) {
            // メール送信に失敗してもユーザーには影響させない（ログ出力のみ）
            logger.error("Failed to start email sender", e);
        }

        return "redirect:/reservation/success"; // 成功ページへリダイレクト
    }

    @GetMapping("/reservation/success") // /reservation/successにGETリクエストが来たとき,予約成功ページを表示する
    public String reservationSuccessPage() {
        return "public/reservation-success"; // 公開用の予約成功ビューを指定
    }
}
