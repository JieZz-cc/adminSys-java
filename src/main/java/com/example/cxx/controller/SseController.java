package com.example.cxx.controller;

import com.example.cxx.service.NoticeService;
import com.example.cxx.sseTask.SseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping
@CrossOrigin
public class SseController {

//    @Autowired
//    private SseClient sseClient;
//
//    // 建立连接
//
//    @GetMapping("/createSse")
//    public SseEmitter createSse(String uid) {
//        if (uid == null || uid.isEmpty()) {
//            log.warn("创建SSE连接失败，UID为空");
//        }
//        SseEmitter sse = sseClient.createSse(uid);
//        log.info("为用户UID: {} 创建SSE连接", uid);
//        return sse;
//    }
//
//    @GetMapping("/sendMsg")
//    @ResponseBody
//    public String sseChat(String uid) {
//        if (uid == null || uid.isEmpty()) {
//            log.warn("接收消息失败，UID为空");
//            throw new IllegalArgumentException("UID不能为空");
//        }
//        for (int i = 0; i < 10; i++) {
////            try {
////                sseClient.sendMessage(uid, "no" + i);
////                Thread.sleep(2000L);
////            }   catch (Exception e) {
////                e.printStackTrace();
////            }
//            sseClient.sendMessage(uid, "no"+i);
//        }
//        return "ok";
//    }
//
//
//    @GetMapping("/closeSse")
//    public void closeConnect(String uid) {
//        if (uid == null || uid.isEmpty()) {
//            log.warn("关闭SSE连接失败，UID为空");
//            throw new IllegalArgumentException("UID不能为空");
//        }
//        sseClient.closeSse(uid);
//        log.info("为用户UID: {} 关闭SSE连接", uid);
//    }

    @Autowired
    private NoticeService noticeService;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @GetMapping("/sse")
    public SseEmitter handleSse(@RequestParam String clientId) {
        SseEmitter emitter = new SseEmitter();

        // 移除旧的连接（如果有）
        emitters.remove(clientId);

        // 添加新的连接到map中
        emitters.put(clientId, emitter);

        // 使用线程池来模拟数据推送
        executorService.submit(() -> {
            try {
                // 模拟数据推送
                for (int i = 0; i < 10; i++) {
                    String message = "Message " + i + " for client " + clientId;
                    emitter.send(SseEmitter.event().data(message));
                    Thread.sleep(3000); // 每秒推送一次数据
                }

                // 完成推送后关闭连接
                emitter.complete();
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            } finally {
                // 从map中移除连接
                emitters.remove(clientId);
            }
        });
        return emitter;
    }

}
