package com.thoughtworks.campus.model;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//创建单个对象的注解，通用的组件对象的创建
@Component
//任务类
public class Task {
    private long id;
    private String content;
    private LocalDateTime updatedAt;

    public Task() {
    }

    public Task(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
//返回时间
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
//设置时间
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setContent(String content) {
        this.content = content;
    }
}
