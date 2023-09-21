package com.todoapp.todo.dto;

import com.todoapp.todo.model.Todo;

public interface TodoMapper {
    Todo mapToTodo(TodoRequest request);
    TodoResponse mapToTodoResponse(Todo todo);
}
