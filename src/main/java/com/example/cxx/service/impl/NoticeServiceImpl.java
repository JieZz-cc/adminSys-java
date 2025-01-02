package com.example.cxx.service.impl;

import com.example.cxx.mapper.NoticeMapper;
import com.example.cxx.pojo.Notice;
import com.example.cxx.pojo.ResPage;
import com.example.cxx.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public void addNewNotice(Notice notice) {
        LocalDateTime now = LocalDateTime.now();
        notice.setCreateTime(now);
        noticeMapper.addNewNotice(notice);
    }

    // 查询单个
    @Override
    public Notice queryNoticeByName(String title) {
        Notice n = noticeMapper.queryNoticeByName(title);
        return n;
    }

    @Override
    public ResPage<Notice> getNotices(Map<String, Object> map) {
        ResPage<Notice> rp = new ResPage<>();
        Integer pageNum = (Integer) map.get("pageNum");
        Integer pageSize = (Integer) map.get("pageSize");
        String title = (String) map.get("title");
        Integer noticeType = (Integer) map.get("noticeType");
        Integer status = (Integer) map.get("status");
        Integer toRange = (Integer) map.get("toRange");
        Integer priority = (Integer) map.get("priority");
        Integer offSet;
        if (pageNum != null ) {
            offSet = (pageNum - 1) * pageSize;
        } else {
            offSet = null;
        }
        Integer total = noticeMapper.getNoticeCounts(title, noticeType, status, toRange, priority);
        List<Notice> list = noticeMapper.getNotices(offSet, pageSize, title, noticeType, status, toRange, priority);
        rp.setTotal(total);
        rp.setItems(list);
        return rp;
    }

    @Override
    public void editNoticeById(Notice notice) {
        LocalDateTime now = LocalDateTime.now();
        notice.setUpdateTime(now);
        noticeMapper.editNoticeById(notice);
    }

    @Override
    public void deleteNoticeById(Integer id) {
        noticeMapper.deleteNoticeById(id);
    }

    @Override
    public void deleteNoticeInBatch(int[] ids) {
        noticeMapper.deleteNoticeInBatch(ids);
    }
}
