package com.example.cxx.sseTask;


import com.aliyuncs.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j      // （Simple Logging Facade for Java）日志框架，lombok依赖
@Component
public class SseClient {
    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    // 建立连接
    public SseEmitter createSse(String uid) {
        // 默认30s超时，0L永不超时
        SseEmitter sseEmitter = new SseEmitter();

        sseEmitter.onCompletion(() -> {
            // 完成后回调
            log.info("sse连接成功");
            sseEmitterMap.remove(uid);
        });

        sseEmitter.onTimeout(() -> {
            // 超时回调
            log.info("超时了");
        });

        sseEmitter.onError(throwable -> {
            // 异常回调
            try {
                log.info("连接异常", throwable.toString());
                sseEmitter.send(SseEmitter.event()
                        .id(uid)
                        .name("发生异常！")
                        .data("发生异常请重试！")
                        .reconnectTime(3000)
                );
                sseEmitterMap.put(uid, sseEmitter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(5000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("创建sse连接成功！", uid);
        sseEmitterMap.put(uid, sseEmitter);
        return sseEmitter;
    }

    // 发送消息
    public boolean sendMessage(String uid, String message) {
        if (StringUtils.isEmpty(message)) {
            log.info("message为空");
            return false;
        }
        SseEmitter sseEmitter = sseEmitterMap.get(uid);
        if (sseEmitter == null) {
            log.info("没有创建连接，请重试。", uid);
            return false;
        }
        try {
            sseEmitter.send(SseEmitter.event().id(uid).reconnectTime(1*60*1000L).data(message));
            log.info("推送成功:{}", uid, message);
            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 关闭连接
    public void closeSse(String uid) {
        if (sseEmitterMap.containsKey(uid)) {
            SseEmitter sseEmitter = sseEmitterMap.get(uid);
            sseEmitter.complete();
            sseEmitterMap.remove(uid);
        } else {
            log.info("连接已关闭",uid);
        }
    }
}
