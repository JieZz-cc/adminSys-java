package com.example.cxx.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice {
    private Integer id;
    private String title;
    private Integer noticeType;
    private Integer status;
    private Integer toRange;
    private Integer priority;
    private String publisher;
    private String parsedPublisher;
    private String content;
    private Boolean isRead;
    private String[] receiverList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
}
