package com.Restaurant.RestaurantSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.validation.constraints.Min; 
import jakarta.validation.constraints.NotBlank;

@Entity //このクラスがデータベースのテーブル（テーブル名はクラス名）にマッピングされることを示す
@Data // Lombokでゲッター/セッター/equals/hashCode/toStringを自動生成

public class Menu {

    @Id //主キーを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主キーを自動インクリメントで生成
    private Long id; // メニューの一意ID

    @NotBlank(message = "料理名は必須です") // 料理名は空であってはならない
    private String name; //料理名

    @Min(value = 0, message = "価格は0以上である必要があります") // 価格は0以上である必要がある
    private Integer price; //価格（整数型を指定。候補：Double,BigDecimal）

    // デフォルトコンストラクタ（JPA必須）
    public Menu() {}

    // コンストラクタ（nameとpriceを指定して作成可能）
    public Menu(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
