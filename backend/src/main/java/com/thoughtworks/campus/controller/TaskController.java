package com.thoughtworks.campus.controller;

import com.thoughtworks.campus.model.Task;
import com.thoughtworks.campus.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

//用在类上，用在控制器上，将控制中所有方法的返回值转换为json并响应到前端
//相当于controller与responsebody的组合
@RestController
//RequestMapping是一个用来处理请求地址映射的注解，可用于类或方法上。用于类上，
//表示类中的所有响应请求的方法都是以该地址作为父路径。
@RequestMapping( "/api/tasks" )
public class TaskController {
//@Autowired 注释，它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作。
    @Autowired
    public TaskService taskService;
//@GetMapping用于处理请求方法的GET类型
//produces指定返回值类型和返回值编码,此处返回json数据
    @GetMapping(produces = "application/json")
    public List<Task> list() {
//输出lsit列表
        return taskService.getAll();
    }
//api/tasks/{id}时
    @GetMapping("/{id}")
//@PathVariable绑定URI模板变量值
    public ResponseEntity<Task> find(@PathVariable Long id) {
//ResponseEntity标识整个http相应：状态码、头部信息以及相应体内容。因此我们可以使用其对http响应实现完整配置。
        return ResponseEntity.of(taskService.find(id));
    }
//post请求
//consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
//produces:  指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；
//@requestBody可以以简单对象接收前端传过来的json数据
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Task> create(@RequestBody Task task) {
//添加内容
        taskService.saveTask(task);
//UriComponentsBuilder构建器与UriComponents类(不可变的URI组件容器)一起工作。通过细粒度的控制URI的各个要素，如构建、扩展模板变量以及编码
//getCurrentRequest
//protected static HttpServletRequest getCurrentRequest()Obtain current request through RequestContextHolder.

//path(String path) 
//Set the path to use instead of the "rawPath" of the URI of the request with the following conditions:
//If uri is also set, the path given here overrides the path of the given URI.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/tasks/{id}")
                .buildAndExpand(task.getId())
                .toUri();
//Build the response entity with no body.
        return ResponseEntity.created(location).build();
    }
//@PutMapping： 和PostMapping作用等同，都是用来向服务器提交信息。如果是添加信息，倾向于用@PostMapping，如果是更新信息，倾向于用@PutMapping。两者差别不是很明显。
    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
//更新内容
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task) {
//Optional 类是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象。
        Optional<Task> updatedTask = taskService.update(new Task(id, task.getContent()));
        return ResponseEntity.of(updatedTask);
    }
//删除
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Task> delete(@PathVariable Long id) {
        Optional<Task> deletedTask = taskService.delete(id);
        if (deletedTask.isPresent()) {
//noContent()
//Create a builder with a NO_CONTENT status.
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
