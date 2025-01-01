package com.example.cxx.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Menu <T> {
    private Integer id;
    private String menuId;
    private String path;
    private Integer parentId;
    private Integer sort;
    private String title;
    private String icon;
    private Integer isLink;
    private Integer isEnable;
    private Integer isAffix;
    private Integer isKeepAlive;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private List<T> children;

}
