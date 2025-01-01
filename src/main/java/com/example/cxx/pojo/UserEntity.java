package com.example.cxx.pojo;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserEntity {
    @ExcelIgnore
    private Integer id;
    @ExcelProperty("用户ID")
    private String userId;

    @ExcelProperty("用户名称")
    @ColumnWidth(20)
    private String username;

    @ExcelProperty("是否管理员")
    private Integer isSuper;

    @ExcelProperty("密码")
    private String password;

    @ExcelProperty("部门ID")
    private Integer deptId;

    @ExcelProperty("部门名称")
    @ColumnWidth(20)
    private String deptName;

    @ExcelProperty("角色ID")
    private String roleId;

    @ExcelProperty("角色名称")
    @ColumnWidth(20)
    private String roleName;

    @ExcelProperty("邮箱")
    @ColumnWidth(20)
    private String email;

    @ExcelProperty("电话")
    @ColumnWidth(20)
    private String phone;

    @ExcelIgnore
    private String avatar;

    @ExcelProperty("备注")
    @ColumnWidth(20)
    private String remark;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private LocalDateTime createTime;

    @ExcelProperty("修改时间")
    @ColumnWidth(20)
    private LocalDateTime updateTime;

}
