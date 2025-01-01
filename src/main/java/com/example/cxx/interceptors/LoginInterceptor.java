package com.example.cxx.interceptors;

import com.example.cxx.utils.JwtUtil;
import com.example.cxx.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

// 拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        try {
            // 从redis中获取token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if(redisToken == null) throw  new RuntimeException();

            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 将业务数据存储到threadlocal中
            ThreadLocalUtil.set(claims);

        } catch(Exception e) {
            e.printStackTrace();
            response.setStatus(401);
            return false;
        } finally {

        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空threadlocal
        ThreadLocalUtil.remove();
    }
}
