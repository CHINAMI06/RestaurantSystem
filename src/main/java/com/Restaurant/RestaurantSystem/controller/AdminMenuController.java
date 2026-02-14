package com.Restaurant.RestaurantSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Restaurant.RestaurantSystem.entity.Menu;
import com.Restaurant.RestaurantSystem.service.MenuService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/menu") // ベースURLを指定
public class AdminMenuController {
    
    @Autowired // Springの依存注入を使用してMenuServiceを注入
    private MenuService menuService; // メニュー関連のサービスを注入

    @GetMapping ///admin/menuにGETリクエストが来たとき,一覧を表示する
    public String ListMenus(Model model) {
        model.addAttribute("menus", menuService.getAllMenus()); // Serviceから全メニューを取得し、モデルに追加
        return "admin/menu-list"; // Thymeleafテンプレート名（templates/admin/menu-list.html）
    }

    @GetMapping("/add") // /admin/menu/addにGETリクエストが来たとき,登録フォームを表示する
    public String AddMenuForm(Model model) {
        model.addAttribute("menu", new Menu()); // 空のMenuオブジェクトをモデルに追加
        return "admin/menu-form"; // Thymeleafテンプレート名（templates/admin/menu-form.html）
    }

    @PostMapping("/add") // /admin/menu/addにPOSTリクエストが来たとき,登録処理を行う
    public String saveMenu(@Valid @ModelAttribute("menu") Menu menu, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { // バリデーションエラーがある場合
            return "admin/menu-form"; // フォームに戻る
        }
        menuService.saveMenu(menu); // Service経由でメニューを保存
        return "redirect:/admin/menu"; // メニュー一覧画面にリダイレクト
    }

}
