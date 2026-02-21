package com.Restaurant.RestaurantSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Restaurant.RestaurantSystem.entity.User;
import com.Restaurant.RestaurantSystem.service.UserService;

import jakarta.validation.Valid;

// 管理者がユーザーを管理するためのコントローラー。ユーザーの一覧表示、作成、編集、削除などの機能を提供する。
@Controller
@RequestMapping("/admin/users")// 管理者用のユーザー管理ページのURLパスを指定
public class AdminUserController {

    // UserServiceを注入して、ユーザー関連のビジネスロジックを呼び出せるようにする
    @Autowired
    private UserService userService;

    // ユーザー一覧を表示
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user-list";
    }

    // ユーザー作成フォームを表示
    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-form";
    }

    // ユーザーを作成
    @PostMapping("/add")
    // @Validアノテーションを使用して、ユーザーオブジェクトのバリデーションを行う。BindingResultはバリデーションの結果を保持する。
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        // バリデーションエラーがある場合は、ユーザー作成フォームに戻る
        if (bindingResult.hasErrors()) {
            return "admin/user-form";
        }
        // 新規作成時はパスワードが必須
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "error.password", "パスワードは必須です");
            return "admin/user-form";
        }
        // ユーザー名が既に存在するかチェック
        if (userService.userExistsByUsername(user.getUsername())) {
            // エラーを紐付ける対象のプロパティ名は"username"、エラーコードは"error.user"、デフォルトのエラーメッセージは"このユーザー名は既に登録済みです"
            bindingResult.rejectValue("username", "error.user", "このユーザー名は既に登録済みです");
            // バリデーションエラーがある場合は、ユーザー作成フォームに戻る
            return "admin/user-form";
        }
        // ユーザーを作成する前に、パスワードをエンコードして保存する。これにより、ユーザーパスワードが安全に保存されるようになる。
        userService.createUser(user);
        return "redirect:/admin/users";
    }

    // ユーザー編集フォームを表示
    @GetMapping("/edit/{id}")
    // @PathVariableアノテーションを使用して、URLの{id}部分をメソッドの引数idにバインドする。ユーザーIDに基づいてユーザー情報を取得し、モデルに追加して編集フォームに渡す。
    public String editUserForm(@PathVariable Long id, Model model) {
        // ユーザーが存在しない場合はユーザー一覧にリダイレクトする。
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        // ユーザー情報をモデルに追加して、編集フォームに渡す。
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    // ユーザーを更新
    @PostMapping("/edit/{id}")
    // @Validアノテーションを削除して手動バリデーションに変更（パスワード空欄を編集時は許可する必要があるため）
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, BindingResult bindingResult) {
        // 手動バリデーション（ユーザー名とロールのみ）
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            bindingResult.rejectValue("username", "error.username", "ユーザー名は必須です");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            bindingResult.rejectValue("role", "error.role", "ロールは必須です");
        }

        // バリデーションエラーがある場合は、ユーザー編集フォームに戻る
        if (bindingResult.hasErrors()) {
            return "admin/user-form";
        }

        // パスワードが空欄の場合、既存のパスワードを保持（パスワード変更なし）
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            User existingUser = userService.getUserById(id);
            user.setPassword(existingUser.getPassword());
        }

        // ユーザーを更新
        userService.updateUser(id, user);
        return "redirect:/admin/users";
    }

    // ユーザーを削除
    @GetMapping("/delete/{id}")
    // @PathVariableアノテーションを使用して、URLの{id}部分をメソッドの引数idにバインドする。ユーザーIDに基づいてユーザーを削除し、ユーザー一覧にリダイレクトする。
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
