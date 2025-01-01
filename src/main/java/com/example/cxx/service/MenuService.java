package com.example.cxx.service;

import com.example.cxx.pojo.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getAuthMenuById(String userId);

    List<Menu> getAllMenu(String name, Integer isEnable, Integer isKeepAlive, Integer isLink);

    void addRoleMenu(String roleId, String[] menuList);

    void addMenu(Menu menu);

    void updateMenu(Menu menu);

    void deleteMenuById(Integer id);

    Menu queryMenuById(String menuId);
}
