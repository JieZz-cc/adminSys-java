package com.example.cxx.service.impl;

import com.example.cxx.mapper.UserMapper;
import com.example.cxx.pojo.ResPage;
import com.example.cxx.pojo.User;
import com.example.cxx.pojo.UserEntity;
import com.example.cxx.service.UserService;
import com.example.cxx.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByUserId(String userId) {
//        Map<String, Object> map = ThreadLocalUtil.get();
        User u = userMapper.queryUserByUserId(userId);
        return u;
    }

    @Override
    public User queryUserById(Integer id) {
       return userMapper.queryUserById(id);
    }

    @Override
    public void updatePassword(String new_pwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updatePassword(new_pwd, id);
    }

    @Override
    public void updateUserInfo(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateUserInfo(user);
    }

    @Override
    public void updateUserRole(String userId, String roleId) {
        userMapper.updateUserRole(userId, roleId);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    @Override
    public void deleteUserInBatch(Map<String, int[]> map) {
        int[] ids = map.get("ids");
        userMapper.deleteUserInBatch(ids);
    }

    @Override
    public ResPage<User> getUserList(int[] deptIds, Integer isSuper, Integer pageSize, Integer pageNum, String name) {
        ResPage<User> rp = new ResPage<>();
//        if (pageSize != null && pageNum != null) {
//            PageHelper.startPage(pageNum, pageSize);    // 开启分页查询
//            List<User> list = userMapper.getUserList(deptIds, isSuper, name);
//            // page中的方法，可以获取pagehelper分页查询后得到的总记录数和当前页数据
//            Page<User> p = (Page<User>) list;
//            rp.setTotal(p.getTotal());
//            rp.setItems(p.getResult());
//        } else {
        // offset 偏移量
        Integer offSet;
        if (pageNum != null) {
            offSet = (pageNum - 1) * pageSize;
        } else {
            offSet = null;
        }
            Integer total = userMapper.getUserTotal();
            List<User> list = userMapper.getUserList(deptIds, isSuper, name, offSet, pageSize);
            rp.setTotal(total);
            rp.setItems(list);
//        }
        return rp;
    }

    @Override
    public void addNewUser(User user) {
        userMapper.addNewUser(user);
    }

    @Override
    public void addUserRole(String userId, String roleId) {
        userMapper.addUserRole(userId, roleId);
    }

    @Override
    public ResPage<UserEntity> getUserEntityList(int[] deptIds, Integer isSuper, Integer pageSize, Integer pageNum, String name) {
        ResPage<UserEntity> rp = new ResPage<>();
        Integer offSet;
        if (pageNum != null) {
            offSet = (pageNum - 1) * pageSize;
        } else {
            offSet = null;
        }
        List<UserEntity> list = userMapper.getUserEntityList(deptIds, isSuper, name, offSet, pageSize);
        rp.setItems(list);
        return rp;
    }

    @Override
    public void importUsers(List<UserEntity> list) {
        userMapper.importUsers(list);
    }
}
