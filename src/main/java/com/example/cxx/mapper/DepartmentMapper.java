package com.example.cxx.mapper;

import com.example.cxx.pojo.Department;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DepartmentMapper {
//    @Select("select * from sys_department")
    List<Department> getDepartmentList(Integer isEnable);

    @Delete("delete from  sys_department where id = #{id}")
    void deleteDepartment(Integer id);

    void updateDepartment(Integer id, String name, Integer parentId, Integer sort, Integer isEnable, LocalDateTime now);

    @Insert("insert into sys_department (name, parent_id, sort, is_enable, create_time)" +
            "values (#{name}, #{parentId}, #{sort}, #{isEnable}, now())")
    void addNewDepartment(Department dp);

    @Select("select * from sys_department where name = #{id}")
    Department queryDepartmentById(String id);

    int[] getDepartmentIds(Integer id);
}
