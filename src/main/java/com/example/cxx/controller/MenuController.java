package com.example.cxx.controller;

import com.example.cxx.pojo.Menu;
//import com.example.cxx.pojo.ResPage;
import com.example.cxx.pojo.Result;
import com.example.cxx.pojo.User;
import com.example.cxx.service.MenuService;
import com.example.cxx.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class MenuController {

    // RequestParam，参数在请求路径？后面
    // RequestBody， 参数在请求体中{}
    // requestHeader，参数在请求头中

    @Autowired
    private MenuService menuService;

    // get请求中请求体不能用RequestBody，要用RequestParam
    @PostMapping("/getAuthMenuById")
    Result<List<Menu>> getAuthMenuById(@RequestBody User user) {
        String userId = user.getUserId();
        List<Menu> flatData = menuService.getAuthMenuById(userId);
        List<Menu> treeData = new ArrayList<>();
        if (flatData != null && flatData.size() > 0) treeData = ListUtils.listToTree(flatData, flatData.get(0).getParentId());
        return Result.success(treeData);
    }

    @GetMapping("/getAllMenu")
    Result getAllMenu(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer isEnable,
            @RequestParam(required = false) Integer isKeepAlive,
            @RequestParam(required = false) Integer isLink
    ) {
      List<Menu> list =  menuService.getAllMenu(name, isEnable, isKeepAlive, isLink);
      List<Menu> treeData = new ArrayList<>();
      if (list != null && list.size() > 0) {
          treeData = ListUtils.listToTree(list, list.get(0).getParentId());
      }
      return Result.success(treeData);
    }

    @PostMapping("/addMenu")
    Result addMenu(@RequestBody Menu menu) {
        Menu item = menuService.queryMenuById(menu.getMenuId());
        if (item != null) return Result.error("已存在重复的菜单id");
        menuService.addMenu(menu);
        return Result.success("新增成功");
    }

    @PutMapping("/updateMenu")
    Result updateMenu(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return Result.success("修改成功");
    }

    @DeleteMapping("/deleteMenuById")
    Result deleteMenuById(@RequestBody Map<String, Integer> map) {
        Integer id = map.get("id");
        menuService.deleteMenuById(id);
        return Result.success("删除成功");
    }


}
