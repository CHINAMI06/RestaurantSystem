package com.Restaurant.RestaurantSystem.repository;

import com.Restaurant.RestaurantSystem.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// SpringがこのインターフェースをBeanとして管理するためのアノテーション。
// データベース独自のエラーをSpring 共通の例外に変換されわかりやすくなる。
@Repository

//Menu がEntityクラスで、主キーの型が Long であることを指定
public interface MenuRepository extends JpaRepository<Menu, Long> {
    
    // 基本CRUDメソッドは継承で自動提供される
    // 必要に応じてカスタムクエリメソッドを追加可能（例: List<Menu> findByName(String name);）

}

