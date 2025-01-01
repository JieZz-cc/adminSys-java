package com.example.cxx.service.impl;

import com.example.cxx.mapper.MenuMapper;
import com.example.cxx.mapper.UserMapper;
import com.example.cxx.pojo.Menu;
import com.example.cxx.service.MenuService;
import com.example.cxx.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Menu> getAuthMenuById(String userId) {
        List<Menu> res = menuMapper.getAuthMenuById(userId);
        return res;
    }

    @Override
    public List<Menu> getAllMenu(String name, Integer isEnable, Integer isKeepAlive, Integer isLink) {
        List<Menu> list = menuMapper.getAllMenu(name, isEnable, isKeepAlive, isLink);
        return list;
    }

    @Override
    public void addRoleMenu(String roleId, String[] menuList) {
        menuMapper.addRoleMenu(roleId, menuList);
    }

    @Override
    public void addMenu(Menu menu) {
        menuMapper.addMenu(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        // 从本地缓存查询当前用户id
//        Map<String, Object> map = ThreadLocalUtil.get();
        LocalDateTime now = LocalDateTime.now();
        menu.setUpdateTime(now);
        menuMapper.updateMenu(menu);
    }

    @Override
    public void deleteMenuById(Integer id) {
        menuMapper.deleteMenuById(id);
    }

    @Override
    public Menu queryMenuById(String menuId) {
       Menu menu = menuMapper.queryMenuById(menuId);
       return menu;
    }
}
