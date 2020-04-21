import {doRequest, doRequestWithBody, doDeleteRequest} from './BaseApi'

const TODO_PATH = "/api/tasks";
//使用 fetch 封装网络请求，返回promise 对象
//前后端连接
//整体获取
export const getTodos = () => {
//直接返回数据
//deferred.then() 函数当Deferred（延迟）对象被解决，拒绝或仍在进行中时，调用添加处理程序。
     return doRequest(TODO_PATH,null)
    .then(response => response.json());
}
//新增
export const addTodo = (todo) => {
	return doRequestWithBody(TODO_PATH, 'post', todo);
}
//修改
//修改后也要刷新页面
export const updateTodo = (todo) => {
	return doRequestWithBody(TODO_PATH + "/" + todo.id, 'put', todo)
    .then(response => response.json());
}
//删除
export const deleteTodo = (todoId) => {	
    return doDeleteRequest(TODO_PATH + "/" + todoId);
}