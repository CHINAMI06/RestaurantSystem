package com.Restaurant.RestaurantSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Restaurant.RestaurantSystem.entity.User;
import com.Restaurant.RestaurantSystem.repository.UserRepository;

// ユーザーの作成、更新、削除、取得などの操作を提供する
@Service
public class UserService {

    // UserRepositoryを注入して、ユーザー情報をデータベースから取得・保存できるようにする
    @Autowired
    private UserRepository userRepository;

    // パスワードを安全に保存するためのエンコーダーを注入。これにより、ユーザーパスワードをハッシュ化して保存できるようになる。
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 全ユーザーを取得
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // IDでユーザーを取得
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // ユーザーを作成（パスワードをエンコード）
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));// パスワードをハッシュ化して保存
        // 役割が指定されていない場合は、デフォルトでROLE_USERを設定
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        return userRepository.save(user);
    }

    // ユーザーを更新
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);// ユーザーが存在しない場合はnullを返す
        if (user == null) {
            return null;
        }
        user.setUsername(userDetails.getUsername());
        // パスワード更新フラグ：新しいパスワードが平文で来た場合のみエンコード。既存パスワード（エンコード済み）が来た場合はそのままセット
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            // パスワードがBCryptハッシュ形式でない場合のみエンコードする（新しいパスワード）
            if (!userDetails.getPassword().startsWith("$2a$") && !userDetails.getPassword().startsWith("$2b$") && !userDetails.getPassword().startsWith("$2y$")) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            } else {
                // 既にエンコード済みパスワード（既存値）の場合はそのままセット
                user.setPassword(userDetails.getPassword());
            }
        }
        // 役割が指定されている場合のみ更新する。空の場合は既存の役割を保持する。
        if (userDetails.getRole() != null && !userDetails.getRole().isEmpty()) {
            user.setRole(userDetails.getRole());
        }
        return userRepository.save(user);
    }

    // ユーザーを削除
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ユーザーが存在するか確認（ユーザー名）
    public boolean userExistsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
