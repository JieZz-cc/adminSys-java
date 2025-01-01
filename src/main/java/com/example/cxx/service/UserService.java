package com.example.cxx.service;

import com.example.cxx.pojo.ResPage;
import com.example.cxx.pojo.User;
import com.example.cxx.pojo.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserService {


    User queryUserByUserId(String userId);


    void updatePassword(String new_pwd);

    void updateUserInfo(User user);


    void deleteUser(Integer id);

    ResPage<User> getUserList(int[] deptIds, Integer isSuper, Integer pageSize, Integer pageNum, String name);

    void addNewUser(User user);


    void addUserRole(String userId, String roleId);

    void deleteUserInBatch(Map<String, int[]> map);

    void updateUserRole(String userId, String roleId);

    User queryUserById(Integer id);

    ResPage<UserEntity> getUserEntityList(int[] deptIds, Integer isSuper, Integer pageSize, Integer pageNum, String name);

    void importUsers(List<UserEntity> list);
}
