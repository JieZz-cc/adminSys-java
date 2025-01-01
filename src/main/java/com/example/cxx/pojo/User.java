package com.example.cxx.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String userId;
    private String username;
    private Integer isSuper;
    @Pattern(regexp = "^\\S{6,16}$")
    private String password;
    private Integer deptId;
    private String deptName;
    private String roleId;
    private String roleName;
    @Email
    private String email;
    private String phone;
    private String avatar;
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private User user;
}
