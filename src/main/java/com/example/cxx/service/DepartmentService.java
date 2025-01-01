package com.example.cxx.service;

import com.example.cxx.pojo.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getDepartmentList(Integer isEnable);

    void deleteDepartment(Integer id);

    void updateDepartment( Integer id, String name, Integer parentId, Integer sort, Integer isEnable);

    void addNewDepartment(Department dp);

    Department queryDepartmentById(String id);

    int[] getDepartmentIds(Integer id);
}
