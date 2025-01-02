package com.example.cxx.service;

import com.example.cxx.pojo.Notice;
import com.example.cxx.pojo.ResPage;

import java.util.Map;

public interface NoticeService {
    void addNewNotice(Notice notice);

    ResPage<Notice> getNotices(Map<String, Object> notice);

    void editNoticeById(Notice notice);

    Notice queryNoticeByName(String title);

    void deleteNoticeById(Integer id);


    void deleteNoticeInBatch(int[] ids);
}
