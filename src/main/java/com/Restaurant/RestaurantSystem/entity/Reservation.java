package com.Restaurant.RestaurantSystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "reservation") // テーブル名を指定（省略するとクラス名がテーブル名になる）
@Data
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 予約の一意ID

    @NotNull(message = "予約日時は必須です")
    @Future(message = "予約日時は未来の日付でなければなりません")
    private LocalDateTime reservationDateTime; // 予約日時


    @NotBlank(message = "お客様名は必須です")
    @Size(max = 50, message = "お客様名は50文字以内で入力してください")
    private String customerName; // お客様名

    @Min(value = 1, message = "人数は1人以上でなければなりません")
    private Integer numberOfPeople; // 人数

    @NotBlank(message = "連絡先は必須です")
    @Email(message = "有効なメールアドレスを入力してください")
    private String contactInfo; // 連絡先（メールアドレス）


}
