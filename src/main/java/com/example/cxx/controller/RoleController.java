package com.example.cxx.controller;

import com.example.cxx.pojo.ResPage;
import com.example.cxx.pojo.Result;
import com.example.cxx.pojo.Role;
import com.example.cxx.service.MenuService;
import com.example.cxx.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @PostMapping("/addRole")
    Result addRole(@RequestBody Role item) {
        Role r = roleService.queryRole(item.getRoleId());
        if (r != null) return Result.error("已存在该角色");
        roleService.addRole(item);
        String roleId = item.getRoleId();      // 自增主键返回
        String[] menuList = item.getMenuList();
        menuService.addRoleMenu(roleId, menuList);
        return Result.success("新增角色成功");
    }

    // 获取角色菜单
    @GetMapping("/getRoleList")
    Result<ResPage<Role>> getRoleList(
            @RequestParam(required = false) Integer isSuper,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) String name
    ) {
        ResPage<Role> list = roleService.getRoleList(isSuper,pageSize,pageNum,name);
        return Result.success(list);
    }

    // 删除角色
    @DeleteMapping("/deleteRole")
    Result deleteRole(@RequestBody Map<String, Integer> map) {
        Integer id = map.get("id");
        roleService.deleteRole(id);
        return Result.success("删除成功");
    }

    // 批量删除角色
    @DeleteMapping("/deleteRoleInBatch")
    Result deleteRoleInBatch(@RequestBody Map<String, int[]> map) {
        int[] ids = map.get("ids");
        roleService.deleteRoleInBatch(ids);
        return Result.success("批量删除成功");
    }

    // 修改角色
    @PutMapping("/updateRole")
    Result updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        String[] newMenus = role.getMenuList();
        if (newMenus != null) {
            String[] oldMenus = roleService.getMenusById(role.getRoleId());
            if (oldMenus.equals(newMenus)) return Result.success("修改成功");
            List<String> delList = new ArrayList<>();
            List<String> addList = new ArrayList<>();
            List<String> oldList = Arrays.asList(oldMenus);
            List<String> newList = Arrays.asList(newMenus);
            if (newMenus.length == 0 && oldMenus.length == 0) {
                return Result.success("修改成功");
            }
            if (newMenus.length == 0 && oldMenus.length > 0) {
                roleService.deleteRoleMenu(role.getRoleId(), oldMenus);
                return Result.success("修改成功");
            }
            if (newMenus.length > 0 && oldMenus.length == 0) {
                menuService.addRoleMenu(role.getRoleId(), newMenus);
                return Result.success("修改成功");
            }
            oldList.forEach(i -> {
                newList.forEach(j -> {
                    if (!newList.contains(i)) delList.add(i);
                    if (!oldList.contains(j)) addList.add(j);
                });
            });
            String[] delRes = delList.stream().distinct().toArray(String[]::new);
            String[] addRes = addList.stream().distinct().toArray(String[]::new);
            if (delRes.length > 0) roleService.deleteRoleMenu(role.getRoleId(), delRes);
            if (addRes.length > 0) menuService.addRoleMenu(role.getRoleId(), addRes);
        }
        return Result.success("修改成功");
    }

    // 查询角色关联菜单
    @GetMapping("/getMenusById")
    Result getMenusById(@RequestParam Map<String, String> map) {
        String roleId = map.get("roleId");
        String[] data = roleService.getMenusById(roleId);
        return Result.success(data);
    }

}
