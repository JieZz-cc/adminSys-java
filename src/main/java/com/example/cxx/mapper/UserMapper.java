package com.example.cxx.mapper;

import com.example.cxx.pojo.User;
import com.example.cxx.pojo.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

//    @Select("select t.* from sys_user t where user_id = #{userId}")
    User queryUserByUserId(String userId);

    @Select("select * from sys_user t where id = #{id}")
    User queryUserById(Integer id);

    @Update("update sys_user set password = #{new_pwd}, update_time = now() where id = #{id}")
    void updatePassword(String new_pwd, Integer id);

//    @Update("update sys_user set name = #{name}, username=#{username},dept_id=#{deptId},role_id")
    void updateUserInfo(User user);

//    @Select("select u.*, d.name as deptName from sys_user u left join sys_department d on u.dept_id = d.id where u.dept_id = #{deptId}")
//    List<User> getUserList(Integer deptId);

    @Delete("delete from sys_user where id = #{id}")
    void deleteUser(Integer id);

    List<User> getUserList(@Param("deptIds") int[] deptIds, @Param("isSuper") Integer isSuper, @Param("name") String name, @Param("offSet") Integer offSet, @Param("pageSize") Integer pageSize);

    // 获取总记录数
    @Select("select count(*) from sys_user;")
    Integer getUserTotal();

    void addNewUser(User user);

    @Insert("insert into sys_user_role (user_id, role_id, create_time) values (#{userId}, #{roleId}, now())")
    void addUserRole(String userId, String roleId);

    void deleteUserInBatch(@Param("ids") int[] ids);

    // 根据用户id查询用户角色id
    @Select("select role_id from sys_user_role where user_id = #{userId}")
    Integer queryRoleIdByUserId(Integer userId);

    @Update("update sys_user_role set role_id = #{roleId} where user_id = #{userId}")
    void updateUserRole(String userId, String roleId);

    List<UserEntity> getUserEntityList(int[] deptIds, Integer isSuper, String name, Integer offSet, Integer pageSize);

    void importUsers(@Param("list") List<UserEntity> list);


}
