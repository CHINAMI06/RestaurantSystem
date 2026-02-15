package com.Restaurant.RestaurantSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Restaurant.RestaurantSystem.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // ユーザー名でユーザーを検索するためのメソッド。Optionalで返すことで、ユーザーが見つからない場合は空のOptionalを返す。
    Optional<User> findByUsername(String username);
}
