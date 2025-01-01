package com.example.cxx.mapper;

import com.example.cxx.pojo.Menu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<Menu> getAuthMenuById(String userId);

//    @Select("select * from sys_menu")
    List<Menu> getAllMenu(@Param("name") String name, @Param("isEnable") Integer isEnable, @Param("isKeepAlive") Integer isKeepAlive, @Param("isLink") Integer isLink);

    void addRoleMenu(@Param("roleId") String roleId, @Param("menus") String[] menuList);

    void addMenu(Menu menu);

    void updateMenu(@Param("menu") Menu menu);

    @Delete("delete from sys_menu where id = #{id}")
    void deleteMenuById(Integer id);

    @Select("select * from sys_menu where menu_id = #{menuId}")
    Menu queryMenuById(String menuId);

//    Integer[] queryMenuByRole(Integer roleId);
}
