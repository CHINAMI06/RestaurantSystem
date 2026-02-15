package com.Restaurant.RestaurantSystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor// 引数なしのコンストラクタを自動生成（JPAがエンティティをインスタンス化するために必要）
@AllArgsConstructor// 全てのフィールドを引数に持つコンストラクタを自動生成（テストやデータ投入に便利）
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ユーザー名は空であってはならず、重複も許さない
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // 例: ROLE_ADMIN, ROLE_USER
    @Column(nullable = false)
    private String role;

}
