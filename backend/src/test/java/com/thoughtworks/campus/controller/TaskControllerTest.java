package com.thoughtworks.campus.controller;

import com.google.gson.Gson;
import com.thoughtworks.campus.model.Task;
import com.thoughtworks.campus.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//对于前后端分离的项目而言，无法直接从前端静态代码中测试接口的正确性，
//因此可以通过MockMVC来模拟HTTP请求。基于RESTful风格的SpringMVC的测试，
//我们可以测试完整的Spring MVC流程，即从URL请求到控制器处理，再到视图渲染都可以测试。

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
//@MockBean
//可以用于向Spring ApplicationContext添加模拟的注释。
//可以用作类级别的注释，也可以用于@Configuration类或@RunWith SpringRunner的测试类中的字段。
    @MockBean
    private TaskService service;

    private List<Task> tasks = new ArrayList<Task>();

    @BeforeEach
    void setUp() {
        tasks.add(new Task(1L, "a"));
    }

    @Test
    public void shouldGetAll() throws Exception {
        when(service.getAll()).thenReturn(tasks);
        this.mockMvc.perform(get("/api/tasks")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("a"));
    }

    @Test
    public void shouldFindTaskByIdIfPresent() throws Exception {
        when(service.find(3L)).thenReturn(Optional.of(new Task(3L, "X")));
        this.mockMvc.perform(get("/api/tasks/3")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("X"));
    }

    @Test
    public void shouldReturnNotFoundWhenFindByIdIfNotPresent() throws Exception {
        when(service.find(3L)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/api/tasks/3")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteWhenExist() throws Exception {
        when(service.delete(2L)).thenReturn(Optional.of(new Task(2L, "B")));
        this.mockMvc.perform(delete("/api/tasks/2")).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeleteIfNotPresent() throws Exception {
        when(service.delete(2L)).thenReturn(Optional.empty());
        this.mockMvc.perform(delete("/api/tasks/2")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateTask() throws Exception {
        Task task = new Task(1L, "new");
        Task savedTask = new Task(1L, "new");
        when(service.saveTask(task)).thenReturn(savedTask);
        this.mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(task)))
                .andDo(print()).andExpect(status().isCreated());
    }

    @Test
    public void shouldChangeTaskById() throws Exception {
        Task task = new Task(2L, "updated");
        Task updated = new Task(1L, "updated");
        when(service.update(any())).thenReturn(Optional.of(updated));
        this.mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(task)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("updated"));
    }

    @Test
    public void shouldReturnNotFoundWhenChangeTaskButDoesNotExit() throws Exception {
        Task task = new Task(2L, "updated");
        when(service.update(any())).thenReturn(Optional.empty());
        this.mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(task)))
                .andDo(print()).andExpect(status().isNotFound());
    }
}