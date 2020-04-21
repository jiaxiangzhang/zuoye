import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import TodoList from './TodoList';
import * as serviceWorker from './serviceWorker';

ReactDOM.render(
  <React.StrictMode> 
    <div className='zhengti'>
    <img src={require('D:/dazuoye/todo-list/frontend/src/1.png')} alt="" width='400px' />  
    <h1> Todo List </h1>
    <TodoList />
    </div>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
