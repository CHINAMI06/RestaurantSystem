package com.Restaurant.RestaurantSystem.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Restaurant.RestaurantSystem.exception.MenuNotFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MenuNotFoundException.class)
    public String handleMenuNotFound(MenuNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage()); // エラーメッセージをフラッシュ属性に追加
        return "redirect:/admin/menu"; // メニュー一覧画面にリダイレクト
    }

}
