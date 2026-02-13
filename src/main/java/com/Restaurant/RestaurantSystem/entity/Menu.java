package com.Restaurant.RestaurantSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity //このクラスがデータベースのテーブル（テーブル名はクラス名）にマッピングされることを示す
@Data // Lombokでゲッター/セッター/equals/hashCode/toStringを自動生成

public class Menu {

    @Id //主キーを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主キーを自動インクリメントで生成
    private Long id; // メニューの一意ID

    private String name; //料理名

    private Integer price; //価格（整数型を指定。候補：Double,BigDecimal）

    // デフォルトコンストラクタ（JPA必須）
    public Menu() {}

    // コンストラクタ（nameとpriceを指定して作成可能）
    public Menu(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
