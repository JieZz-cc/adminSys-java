package com.example.cxx.service;

import com.example.cxx.pojo.ResPage;
import com.example.cxx.pojo.Role;

public interface RoleService {
    ResPage<Role> getRoleList(Integer isSuper, Integer pageSize, Integer pageNum, String name);

    void updateRole(Role role);

    void deleteRole(Integer id);

    void deleteRoleInBatch(int[] ids);

    Role queryRole(String roleId);

    void addRole(Role item);

    String[] getMenusById(String roleId);

//    void updateRoleMenu(Integer roleId, int[] menuList);

    void deleteRoleMenu(String roleId, String[] delRes);
}
