package com.example.cxx.controller;


import com.alibaba.excel.EasyExcel;
import com.example.cxx.listen.ExcelListener;
import com.example.cxx.pojo.*;
import com.example.cxx.service.DepartmentService;
import com.example.cxx.service.UserService;
import com.example.cxx.utils.ExportUtils;
import com.example.cxx.utils.JwtUtil;
import com.example.cxx.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 登录
    @PostMapping("/login")
    Result login(@RequestBody User user) {
        User u = userService.queryUserByUserId(user.getUserId());
        if (u == null) return Result.error("用户名错误");
        if (user.getPassword().equals(u.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("userId", u.getUserId());
            // 生成token
            String token = JwtUtil.genToken(claims);
            // token存储到redis
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 10, TimeUnit.HOURS);

            Map<String, Object> resMap = new HashMap<>();
            resMap.put("user", u);
            resMap.put("token", token);

            return Result.success(resMap);
        }
        return Result.error("密码错误");
    }


    // 查询单个用户
    @GetMapping("/getUserInfo")
    Result<User> getUserInfo(@RequestParam Map<String, String> map) {

        String userId = map.get("userId");
        User u = userService.queryUserByUserId(userId);
        return Result.success(u);
    }

    // 更新密码
    @PatchMapping("/updatePassword")
    Result updatePassword(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        String old_pwd = params.get("old_pwd");
        String new_pwd = params.get("new_pwd");
        String re_pwd = params.get("re_pwd");

        if (!StringUtils.hasLength(old_pwd) || !StringUtils.hasLength(new_pwd) || !StringUtils.hasLength(re_pwd)) {
            return Result.error("缺少入参");
        }

        Map<String, Object> userMap = ThreadLocalUtil.get();
        Integer id = (Integer) userMap.get("id");
        User user = userService.queryUserById(id);
        if (!user.getPassword().equals(old_pwd)) return Result.error("原密码输入错误");
        if (!new_pwd.equals(re_pwd)) return Result.error("两次输入密码不一致");

        userService.updatePassword(new_pwd);

        // 删除之前旧的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }

    // 更新用户信息
    @PutMapping("/updateUserInfo")
    Result updateUserInfo(@RequestBody @Validated User user) {
        userService.updateUserInfo(user);
        if(user.getRoleId() != null) userService.updateUserRole(user.getUserId(), user.getRoleId());
        return Result.success();
    }

    // 获取用户列表
    @GetMapping("/getUserList")
    Result getUserList(
            @RequestParam Integer deptId,
            @RequestParam(required = false) Integer isSuper,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) String name
    )
    {
        // 先递归查询所选部门下所有子部门
        int[] deptIds = departmentService.getDepartmentIds(deptId);
        ResPage<User> list = userService.getUserList(deptIds,isSuper,pageSize,pageNum,name);
//        List<User> list = userService.getUserList(deptId);
        return Result.success(list);
    }

    // 删除单个用户
    @DeleteMapping("/deleteUser")
    Result deleteUser(@RequestBody User user) {
        Integer id = user.getId();
        userService.deleteUser(id);
        return Result.success("删除成功");
    }

    // 批量删除用户
    @DeleteMapping("/deleteUserInBatch")
    Result deleteUserInBatch(@RequestBody Map<String, int[]> map) {
        userService.deleteUserInBatch(map);
        return Result.success("批量删除成功");
    }

    // 新增用户
    @PostMapping("/addNewUser")
    Result addNewUser(@RequestBody User user) {
        User u = userService.queryUserByUserId(user.getUserId());
        if (u != null) return Result.error("已存在该用户");
        userService.addNewUser(user);
        String UserId = user.getUserId();
        String roleId = user.getRoleId();
        userService.addUserRole(UserId, roleId);
        return Result.success("新增成功");
    }

    // 导出用户列表
    @CrossOrigin
    @GetMapping("/exportUsers")
    public void exportUsers(
            HttpServletResponse response,
            @RequestParam Integer deptId,
            @RequestParam(required = false) Integer isSuper,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) String name
    ) throws IOException {
        // 先递归查询所选部门下所有子部门
        int[] deptIds = departmentService.getDepartmentIds(deptId);
//        ResPage<User> list = userService.getUserList(deptIds,isSuper,pageSize,pageNum,name);
        ResPage<UserEntity> list = userService.getUserEntityList(deptIds,isSuper,pageSize,pageNum,name);
        List<UserEntity> data = list.getRows();
        ExportUtils.exportByEasyExcel(response, data, UserEntity.class, "用户表");
    }

    // 导入
    // @RequestPart适用于复杂的请求域（像JSON，XML）
    // @RequestParam适用于name-valueString类型的请求域
    @PostMapping("/importUsers")
    Result importUsers(@RequestPart(value = "file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UserEntity.class, new ExcelListener(this.userService)).sheet().doRead();
        return Result.success("导入成功");
    }

}
