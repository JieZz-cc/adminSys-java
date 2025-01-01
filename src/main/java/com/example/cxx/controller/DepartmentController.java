package com.example.cxx.controller;


import com.example.cxx.pojo.Department;
import com.example.cxx.pojo.Result;
import com.example.cxx.service.DepartmentService;
import com.example.cxx.utils.ExportUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/getDepartmentList")
    Result getDepartmentList(@RequestParam(required = false) Integer isEnable) {
        List<Department> data = departmentService.getDepartmentList(isEnable);
        List<Department> treeData = new ArrayList<>();
        if (data != null && data.size() > 0) treeData = flatToTree(data, data.get(0).getParentId());
        return Result.success(treeData);
    }

    // 查询部门及其下面部门id
    @GetMapping("/getDepartmentIds")
    Result getDepartmentIds(@RequestParam Integer id) {
        int[] data = departmentService.getDepartmentIds(id);
        return Result.success(data);
    }

    @DeleteMapping("/deleteDepartment")
    Result deleteDepartment(@RequestBody Map<String, Integer> map) {
        Integer id = map.get("id");
        departmentService.deleteDepartment(id);
        return Result.success("删除成功");
    }

    @PutMapping("/updateDepartment")
    Result updateDepartment(@RequestBody Department dp) {
        Integer parentId = dp.getParentId();
        String name = dp.getName();
        Integer sort = dp.getSort();
        Integer id = dp.getId();
        Integer isEnable = dp.getIsEnable();
        departmentService.updateDepartment(id, name, parentId, sort, isEnable);
        return Result.success("修改成功");
    }

    @PostMapping("/addNewDepartment")
    Result addNewDepartment(@RequestBody Department dp) {
        Department d = departmentService.queryDepartmentById(dp.getName());
        if (d != null) return Result.error("已存在该部门");
        departmentService.addNewDepartment(dp);
        return  Result.success("新增成功");
    }


    public static List<Department> flatToTree(List<Department> list, Integer parentId) {
        List<Department> treeData = list.stream()
                .filter(item -> item.getParentId() == parentId)
                .map(child -> {
                    child.setChildren(flatToTree(list, child.getId()));
                    return child;
                }).collect(Collectors.toList());
        return treeData;
    }

    //导出
    @Transactional
    @GetMapping("/exportDepartment")
    public void exportData(HttpServletResponse response) throws IOException {
        Integer isEnable = 0;
        List<Department> deptList = departmentService.getDepartmentList(isEnable);
//        ExportUtils.exportData(response, deptList, Department.class);
        ExportUtils.exportByEasyExcel(response, deptList, Department.class, "部门表");
    }
}
