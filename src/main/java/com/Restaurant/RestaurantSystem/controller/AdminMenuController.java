package com.Restaurant.RestaurantSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Restaurant.RestaurantSystem.entity.Menu;
import com.Restaurant.RestaurantSystem.service.MenuService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/menu") // ベースURLを指定
public class AdminMenuController {
    
    private final MenuService menuService;

    public AdminMenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping // /admin/menuにGETリクエストが来たとき,一覧を表示する
    public String listMenus(Model model) {
        model.addAttribute("menus", menuService.getAllMenus()); // Serviceから全メニューを取得し、モデルに追加
        return "admin/menu-list"; // Thymeleafテンプレート名（templates/admin/menu-list.html）
    }

    @GetMapping("/add") // /admin/menu/addにGETリクエストが来たとき,登録フォームを表示する
    public String addMenuForm(Model model) {
        model.addAttribute("menu", new Menu()); // 空のMenuオブジェクトをモデルに追加
        return "admin/menu-form"; // Thymeleafテンプレート名（templates/admin/menu-form.html）
    }

    @GetMapping("/edit/{id}") // /admin/menu/edit/{id}にGETリクエストが来たとき,編集フォームを表示する
    public String editMenuForm(@PathVariable Long id, Model model) {
        Menu menu = menuService.getMenuById(id); // IDに基づいてメニューを取得
        model.addAttribute("menu", menu); // メニューをモデルに追加
        return "admin/menu-form"; // Thymeleafテンプレート名（templates/admin/menu-form.html）
    }

    @PostMapping("/save") // メニュー保存（追加・更新）
    public String saveMenu(@Valid @ModelAttribute("menu") Menu menu,
                            BindingResult bindingResult, 
                            Model model,
                            RedirectAttributes redirectAttributes
                            ) { //@Valid を付けたオブジェクトの すぐ後ろ に BindingResult を置く
        if (bindingResult.hasErrors()) { // バリデーションエラーがある場合
            return "admin/menu-form"; // フォームに戻る
        }
        try {
            menuService.saveMenu(menu); // Service経由でメニューを保存(idあり→更新、idなし→追加)
        } catch (IllegalArgumentException e) { // バリデーションエラーがService層で発生した場合
            model.addAttribute("serviceError", e.getMessage()); // エラーメッセージをモデルに追加
            return "admin/menu-form"; // フォームに戻る
        }

        redirectAttributes.addFlashAttribute("successMessage", "メニューが保存されました。"); // 成功メッセージをフラッシュ属性に追加
        return "redirect:/admin/menu"; // メニュー一覧画面にリダイレクト
    }

    @GetMapping("/delete/{id}") // /admin/menu/delete/{id}にGETリクエストが来たとき,メニューを削除する
    public String deleteMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        menuService.deleteMenu(id); // Service経由でメニューを削除
        redirectAttributes.addFlashAttribute("successMessage", "メニューが削除されました。"); // 成功メッセージをフラッシュ属性に追加
        return "redirect:/admin/menu"; // メニュー一覧画面にリダイレクト
    }

}
