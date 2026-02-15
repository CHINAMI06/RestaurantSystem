package com.Restaurant.RestaurantSystem.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Restaurant.RestaurantSystem.entity.User;
import com.Restaurant.RestaurantSystem.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // UserRepositoryを注入して、ユーザー情報をデータベースから取得できるようにする
    @Autowired
    private UserRepository userRepository;

    // ユーザー名でユーザーを検索し、UserDetailsオブジェクトを返すメソッド。ユーザーが見つからない場合は例外をスローする。
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

        // ユーザーの役割をSimpleGrantedAuthorityとして設定し、Spring Securityが認識できる形式でUserDetailsオブジェクトを返す
        return new org.springframework.security.core.userdetails.User(
                // ユーザー名、パスワード、権限を設定。ここではユーザーのロールを1つの権限として扱う。
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
