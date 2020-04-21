package com.thoughtworks.campus.service;

import com.thoughtworks.campus.model.Task;
import com.thoughtworks.campus.store.TaskStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//业务层组件创建
@Service
public class TaskService {
    @Autowired
    public TaskStore store;
//获取全部内容
//得到list列表
    public List<Task> getAll() {
        return store.readTasks();
    }
//新增列表内容
//设置时间
//增加列表内容
//写出列表
    public Task saveTask(Task task) {
        List<Task> tasks = new ArrayList<>(store.readTasks());
        task.setUpdatedAt();
        tasks.add(task);
        store.writeTasks(tasks);
        return task;
    }
//返回某个id的内容
//findAny()操作，返回的元素是不确定的
//Java 8 API添加了一个新的抽象称为流Stream，可以让你以一种声明的方式处理数据。
    public Optional<Task> find(Long id) {
        return store.readTasks().stream().filter(task -> task.getId() == id).findFirst();
    }
//更新数据
    public Optional<Task> update(Task task) {
        List<Task> tasks = new ArrayList<>(store.readTasks());
//将id值相同的任务替换
//重新设置
//重新写入
        Optional<Task> any = tasks.stream().filter(task1 -> task1.getId() == task.getId()).findAny();
        if (any.isPresent()) {
            any.get().setContent(task.getContent());
            any.get().setUpdatedAt();
            store.writeTasks(tasks);
        }
        return any;
    }
//删除
    public Optional<Task> delete(Long id) {
        List<Task> tasks = store.readTasks();
//查询该ID值
        Optional<Task> any = tasks.stream().filter(task1 -> task1.getId() == id).findAny();
        if (any.isPresent()) {
//将不等于该id的全部写入
//collect是一个终端操作,它接收的参数是将流中的元素累积到汇总结果的各种方式
//将数据收集进一个列表(Stream 转换为 List，允许重复值，有顺序)
            store.writeTasks(tasks.stream().filter(task -> task.getId() != id).collect(Collectors.toList()));
            return any;
        }
        return any;
    }
}
