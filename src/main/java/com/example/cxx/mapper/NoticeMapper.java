package com.example.cxx.mapper;

import com.example.cxx.pojo.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {
//    @Insert("insert into sys_notice (title, notice_type, status, to_range, priority, create_time) values " +
//            "(#{title}, #{noticeType}, #{status},#{toRange},#{priority},#{createTime})")
    void addNewNotice(@Param("notice") Notice notice);

//    @Select("select count(*) from sys_notice")
    Integer getNoticeCounts(String title, Integer noticeType, Integer status, Integer toRange, Integer priority, String publisher);

    List<Notice> getNotices(Integer offSet, Integer pageSize, String title, Integer noticeType, Integer status, Integer toRange, Integer priority, String publisher);

    void editNoticeById(@Param("notice") Notice notice);

    @Select("select * from sys_notice where title = #{title}")
    Notice queryNoticeByName(String title);

    @Delete("delete from sys_notice where id = #{id}")
    void deleteNoticeById(Integer id);

    void deleteNoticeInBatch(@Param("ids") int[] ids);

    void addUserNotice(@Param("id") Integer id, @Param("receiverList") String[] receiverList);

    @Update("update sys_notice set status = 1, publish_time = #{publishTime} where id = #{id}")
    void publishNotice(Notice notice);

    @Select("select user_id from sys_user_notice where notice_id = #{id}")
    String[] getReceiverList(Integer id);

    void deleteUserNotice(@Param("id") Integer id, @Param("list") String[] list);
}
