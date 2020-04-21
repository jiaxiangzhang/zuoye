package com.thoughtworks.campus.store;

import com.google.gson.*;
import com.thoughtworks.campus.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//业务层组件创建
@Service
public class TaskStore {
//@Value(“${xxxx}”)注解从配置文件读取值
    @Value("${todo.store.filename}")
//获取字符串常量
    private String fileName;
    @Value("${env}")
//获取字符串常量
    private String env;
//读取内容
//输出一个list列表
    public List<Task> readTasks() {
        try {
//Files.readAllBytes(Path)方法把整个文件读入内存
//File.toPath() 从改文件中创建一个path对象
            String contents = new String(Files.readAllBytes(getFile().toPath()));
//.fromJson从Json相关对象到Java实体的方法，json字符串和要转换的类型
            Task[] tasks = getGson().fromJson(contents, Task[].class);
//Arrays.asList将数组转化为list
            return Arrays.asList(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
//生成指定类型的空List 
        return Collections.emptyList();
    }
//改变列表内容
    public void writeTasks(List<Task> tasks) {
        try {
// getAbsolutePath():返回抽象路径名的绝对路径名字符串
//文件写出流
//写出制定内容，刷新
            FileWriter fileWriter = new FileWriter(getFile().getAbsolutePath());
            fileWriter.write(getGson().toJson(tasks));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile() {
//测试模式
//getClass() 返回此 Object 的运行时类
//getClassLoader() 获得这个类对象的加载器
//.getClass().getClassLoader().getResource(fileName)：表示只会在根目录下（/）查找该文件（只会接受一个相对路径）
//getFile() 返回URL文件名部分
        if (env.equals("test")) {
            return new File(getClass().getClassLoader().getResource(fileName).getFile());
        }
//根据路径返回文件对象
        return new File(fileName);
    }
//GSON是Google提供的用来在Java对象和JSON数据之间进行映射的Java类库。
//可以将一个Json字符转成一个Java对象，或者将一个Java转化为Json字符串。
    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>)
                        (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                        (localDateTime, typeOfT, context) -> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .create();
    }
}
