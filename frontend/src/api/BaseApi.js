const basePath = (process.env.NODE_ENV === 'production' ? '/todo-list':'');

//生成请求路径以及请求内容
//渲染服务器内容
//fetch请求数据
// fetch("请求的URL"，{// 这个对象是请求的配置信息
//         method: 'GET',  
//         credientials： "include" ，
//         mode: 'no-cors'， 
//         headers: { // 这个对象主要设置请求头的一些信息，例如：content-type、Content-Length、X-Custom-Header等 }，
//         body： 请求的数据（即常用ajax请求部分的data）
// 	}).then(res => res.json()).then(res => res)
//
//发送和接收cookie:加上credentials: 'same-origin'
export const doRequest = (path, params) => {
	let mergedParams = {
//same-origin：如果URL与调用脚本位于相同的源，则发送用户凭证（cookie，基本http认证等）。
		credentials: 'same-origin',
//三个点（...）真名叫扩展运算符，是在ES6中新增加的内容，它可以在函数调用/数组构造时，将数组表达式或者string在语法层面展开；
//还可以在构造字面量对象时将对象表达式按照key-value的方式展开
	    ...params
	 } 
	return fetch(basePath+path, mergedParams);
}

//改变服务器内容
export const doRequestWithBody = (path, method, body) => {
	return doRequest(path, { 
	    headers: {
	      'content-type': 'application/json'
	    },
	    method, 
	    body: JSON.stringify(body)
	})
}

//删除内容
export const doDeleteRequest = (path) => {
	return doRequest(path, { 
	    method: 'delete'
	})
}