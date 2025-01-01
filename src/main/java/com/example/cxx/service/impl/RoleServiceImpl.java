package com.example.cxx.service.impl;

import com.example.cxx.mapper.RoleMapper;
import com.example.cxx.pojo.ResPage;
import com.example.cxx.pojo.Role;
import com.example.cxx.service.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

//    @Override
//    public void addRole(Role role) {
//        roleMapper.addRole(role);
//    }

    @Override
    public ResPage<Role> getRoleList(Integer isSuper, Integer pageSize, Integer pageNum, String name) {

        ResPage<Role> rp = new ResPage<>();
        if (pageSize != null &&  pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);    // 开启分页查询
            List<Role> list = roleMapper.getRoleList(isSuper, name);
            Page<Role> p = (Page<Role>) list;
            rp.setTotal(p.getTotal());
            rp.setItems(p.getResult());
        } else {
            List<Role> list = roleMapper.getRoleList(isSuper, name);
            rp.setItems(list);
            rp.setTotal(list.size());
        }
        return rp;
    }

    @Override
    public void updateRole(Role role) {
//        Integer id = role.getId();
//        String roleId = role.getRoleId();
//        String roleName = role.getRoleName();
//        Integer isSuper = role.getIsSuper();
//        String remark = role.getRemark();
        LocalDateTime now = LocalDateTime.now();
        role.setUpdateTime(now);
//        roleMapper.updateRole(id, roleId, roleName,isSuper,remark,now);
        System.out.println(role);
        roleMapper.updateRole(role);
    }

    @Override
    public void deleteRole(Integer id) {
        roleMapper.deleteRole(id);
    }

    @Override
    public void deleteRoleInBatch(int[] ids) {
        roleMapper.deleteRoleInBatch(ids);
    }

    @Override
    public Role queryRole(String roleId) {
        Role role1 = roleMapper.queryRole(roleId);
        return role1;
    }

    @Override
    public void addRole(Role item) {
        roleMapper.addRole(item);
    }

    @Override
    public String[] getMenusById(String roleId) {
        String[] list = roleMapper.getMenusById(roleId);
        return list;
    }

    @Override
    public void deleteRoleMenu(String roleId, String[] delRes) {
        roleMapper.deleteRoleMenu(roleId, delRes);
    }
}
