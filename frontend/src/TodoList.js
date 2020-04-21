import React, { useState, Fragment, useEffect } from "react";
import TodoItem from "./TodoItem";
import { getTodos, addTodo, deleteTodo, updateTodo } from "./api/TodoApi";
import "./style.css";
import _ from "lodash";

//
// useState 的用法是，需要传入一个参数作为状态的初始值，当函数执行后会返回两个值，一个是当前状态的属性，一个是修改状态的方法。
const TodoList = () => {
  const [inputValue, setInputValue] = useState("");
  const [list, setList] = useState(null);
  const [error, setError] = useState("");
//渲染全部元素
  const handleLoadTasks = () => {
    getTodos()
      .then((response) => {
        setList(response);
      })
      .catch((error) => {
        setError("Unable to retrieve todo's");
      });
  };
//增加元素
  const handleAddTask = () => {
    if (inputValue === "") return;
//增加新元素，同时将输入框清空
    const newTask = {
      id: _.parseInt(list.length ? list[list.length - 1].id : 0) + 1,
      content: inputValue,
    };

    addTodo(newTask).then(() => {
      setList([...list, newTask]);
      setInputValue("");
    }); 
  };
//删除元素
  const handleDeleteTask = (id) =>
    deleteTodo(id).then(() => {
      setList(list.filter((item) => item.id !== id));
    });

  const handleUpdateTask = (task) => {
    if (task.content === "") return;

    updateTodo(task).then((response) => {
      setList(
        list.map((x) => (x.id === response.id ? { ...x, ...response } : x))
      );
    });
  };

  useEffect(() => {
    handleLoadTasks();
  }, []);

  if (list === null) {
    return <div>Tasks is loading ...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
//通过onclick确定事件
// event.target.value 获取文本值
//两部分，新增文本框+新增button
//listitem
    <Fragment>
      <div>
        <input
          className="task-input"
          type="text"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          data-testid="task-input"
        />
        <button className="submit-button" onClick={handleAddTask} data-testid="add-button" >
          提交
        </button>
      </div>
      <ul data-testid="task-items" className="task-items">
        {list.map((item) => (
          <TodoItem
            key={item.id}
            item={item}
            index={item.id}
            onItemDelete={handleDeleteTask}
            onItemUpdate={handleUpdateTask}
          />
        ))}
      </ul>
    </Fragment>
  );
};

export default TodoList;
