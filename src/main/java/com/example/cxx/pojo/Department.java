package com.example.cxx.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.example.cxx.convert.EnableConvert;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Table;


@Data       // lombok插件，在编译阶段自动生成setter、getter、tostring、构造方法
//@TableName()
@Table(name = "部门表")
public class Department {
    @ExcelProperty("id")
    private Integer id;

    @ExcelProperty("部门名")
    private String name;

    @ExcelProperty("父级部门Id")
    private Integer parentId;

    @ExcelIgnore
    private Integer sort;

//    @ExcelProperty(value = "性别", converter = EnableConvert.class)
    @ExcelProperty("是否启用")
    private Integer isEnable;

    @ColumnWidth(20)
    @ExcelProperty("创建日期")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ColumnWidth(20)
    @ExcelProperty("更新日期")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ExcelIgnore
    private List<Department> children;

}
