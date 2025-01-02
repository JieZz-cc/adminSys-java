package com.example.cxx.controller;

import com.example.cxx.pojo.Notice;
import com.example.cxx.pojo.ResPage;
import com.example.cxx.pojo.Result;
import com.example.cxx.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 新增
    @PostMapping("/addNewNotice")
    Result addNewNotice(@RequestBody Notice notice) {
        Notice n = noticeService.queryNoticeByName(notice.getTitle());
        if (n != null) return Result.error("已存在该公告标题");
        noticeService.addNewNotice(notice);
        return Result.success("新增成功，快去发布把");
    }
    // 查询列表
    @PostMapping("/getNotices")
    Result getNotices(@RequestBody Map<String, Object> map) {
        ResPage<Notice> list = noticeService.getNotices(map);
        return Result.success(list);
    }
    // 编辑
    @PutMapping ("/editNoticeById")
    Result editNoticeById(@RequestBody Notice notice) {
        noticeService.editNoticeById(notice);
        return Result.success("编辑成功");
    }

    // 删除单个
    @DeleteMapping ("/deleteNoticeById")
    Result deleteNoticeById(@RequestBody Map<String, Integer> map) {
        Integer id = map.get("id");
        noticeService.deleteNoticeById(id);
        return Result.success("删除成功");
    }

    // 批量删除
    @DeleteMapping("/deleteNoticeInBatch")
    Result deleteNoticeInBatch(@RequestBody Map<String, int[]> map) {
        int[] ids = map.get("ids");
        noticeService.deleteNoticeInBatch(ids);
        return Result.success("删除成功");
    }

}
