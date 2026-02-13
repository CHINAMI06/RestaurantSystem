package com.Restaurant.RestaurantSystem.service;

import com.Restaurant.RestaurantSystem.entity.Menu;
import com.Restaurant.RestaurantSystem.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // このクラスがサービスコンポーネントであることを示す
public class MenuService {

    @Autowired // Repositoryを自動注入
    private MenuRepository menuRepository;

    // DB操作でエラーが起きた場合にロールバックを自動化
    @Transactional //メニューを保存
    public Menu saveMenu(Menu menu) {

        // 価格のバリデーション
        if (menu.getPrice() < 0) {
            throw new IllegalArgumentException("価格は0以上である必要があります。");
        }

        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true) // 全メニュー一覧を取得
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Transactional(readOnly = true) //IDで取得
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);  // 存在しない場合はnull
    }   

    @Transactional //削除
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

}