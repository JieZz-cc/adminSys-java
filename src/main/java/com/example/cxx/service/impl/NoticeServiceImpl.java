package com.example.cxx.service.impl;

import com.example.cxx.mapper.NoticeMapper;
import com.example.cxx.pojo.Notice;
import com.example.cxx.pojo.ResPage;
import com.example.cxx.pojo.Result;
import com.example.cxx.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
        noticeMapper.addUserNotice(notice.getId(), notice.getReceiverList());
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
        String publisher = (String) map.get("publisher");
        Integer offSet;
        if (pageNum != null ) {
            offSet = (pageNum - 1) * pageSize;
        } else {
            offSet = null;
        }
        Integer total = noticeMapper.getNoticeCounts(title, noticeType, status, toRange, priority, publisher);
        List<Notice> list = noticeMapper.getNotices(offSet, pageSize, title, noticeType, status, toRange, priority, publisher);
        rp.setTotal(total);
        rp.setItems(list);
        return rp;
    }

    @Override
    public void editNoticeById(Notice notice) {
        noticeMapper.editNoticeById(notice);
        String[] newUserList = notice.getReceiverList();
        if (newUserList != null) {
            String[] oldUserList = noticeMapper.getReceiverList(notice.getId());
            if (oldUserList.equals(newUserList)) return ;
            List<String> delList = new ArrayList<>();
            List<String> addList = new ArrayList<>();
            List<String> oldList = Arrays.asList(oldUserList);
            List<String> newList = Arrays.asList(newUserList);
            if (newUserList.length == 0 && oldUserList.length == 0) {
                return ;
            }
            if (newUserList.length == 0 && oldUserList.length > 0) {
                noticeMapper.deleteUserNotice(notice.getId(), oldUserList);
                return ;
            }
            if (newUserList.length > 0 && oldUserList.length == 0) {
                noticeMapper.addUserNotice(notice.getId(), newUserList);
                return ;
            }
            oldList.forEach(i -> {
                newList.forEach(j -> {
                    if (!newList.contains(i)) delList.add(i);
                    if (!oldList.contains(j)) addList.add(j);
                });
            });
            String[] delRes = delList.stream().distinct().toArray(String[]::new);
            String[] addRes = addList.stream().distinct().toArray(String[]::new);
//            System.out.println(addRes instanceof );
            if (delRes.length > 0) noticeMapper.deleteUserNotice(notice.getId(), delRes);
            if (addRes.length > 0) noticeMapper.addUserNotice(notice.getId(), addRes);
        }
    }

    @Override
    public void deleteNoticeById(Integer id) {
        noticeMapper.deleteNoticeById(id);
    }

    @Override
    public void deleteNoticeInBatch(int[] ids) {
        noticeMapper.deleteNoticeInBatch(ids);
    }

    @Override
    public void publishNotice(Notice notice) {
        LocalDateTime now = LocalDateTime.now();
        notice.setPublishTime(now);
        noticeMapper.publishNotice(notice);
    }
}
