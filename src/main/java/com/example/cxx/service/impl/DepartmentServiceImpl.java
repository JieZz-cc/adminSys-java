package com.example.cxx.service.impl;

import com.example.cxx.mapper.DepartmentMapper;
import com.example.cxx.pojo.Department;
import com.example.cxx.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getDepartmentList(Integer isEnable) {
        List<Department> res = departmentMapper.getDepartmentList(isEnable);
        return res;
    }

    @Override
    public void deleteDepartment(Integer id) {
        departmentMapper.deleteDepartment(id);
    }

    @Override
    public void updateDepartment( Integer id, String name, Integer parentId, Integer sort, Integer isEnable) {
        LocalDateTime now = LocalDateTime.now();
        departmentMapper.updateDepartment(id, name, parentId, sort, isEnable, now);
    }

    @Override
    public void addNewDepartment(Department dp) {
        departmentMapper.addNewDepartment(dp);
    }

    @Override
    public Department queryDepartmentById(String id) {
        Department d = departmentMapper.queryDepartmentById(id);
        return d;
    }

    @Override
    public int[] getDepartmentIds(Integer id) {
        return departmentMapper.getDepartmentIds(id);
    }
}
