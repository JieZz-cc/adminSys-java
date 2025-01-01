package com.example.cxx.mapper;

import com.example.cxx.pojo.Role;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RoleMapper {

    List<Role> getRoleList(Integer isSuper, String name);

//    void updateRole(Integer id, String roleId, String roleName, Integer isSuper, String remark, LocalDateTime now);
    void updateRole(Role role);

    @Delete("delete from sys_role where id = #{id}")
    void deleteRole(Integer id);

    void deleteRoleInBatch(@Param("ids") int[] ids);

    @Select("select * from sys_role where role_id = #{role}")
    Role queryRole(String roleId);

    void addRole(Role role);

    String[] getMenusById(String roleId);

//    void updateRoleMenu(@Param("roleId") Integer roleId, @Param("menuList") int[] menuList);

    void deleteRoleMenu(@Param("roleId") String roleId, @Param("menus") String[] delRes);
}
