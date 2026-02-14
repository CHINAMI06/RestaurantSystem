package com.Restaurant.RestaurantSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Restaurant.RestaurantSystem.entity.Menu;
import com.Restaurant.RestaurantSystem.service.MenuService;

@Controller
public class PublicMenuController {
    
    @Autowired
    private MenuService menuService; // メニュー関連のサービスを注入
    
    @GetMapping("/menu") // /menuにGETリクエストが来たとき,メニュー一覧を表示する
    public String listMenu(Model model) {
        List<Menu> menus = menuService.getAllMenus(); // Serviceから全メニューを取得
        model.addAttribute("menus", menus); // モデルにメニュー一覧を追加
        return "public/menu-list"; // 公開用のビューを指定
    }
