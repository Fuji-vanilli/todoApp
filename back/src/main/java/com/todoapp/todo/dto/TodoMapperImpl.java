package com.todoapp.todo.dto;

import com.todoapp.todo.model.Todo;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TodoMapperImpl implements TodoMapper{
    @Override
    public Todo mapToTodo(TodoRequest request) {
        return Todo.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    @Override
    public TodoResponse mapToTodoResponse(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .createdDate(todo.getCreatedDate())
                .lastUpdate(todo.getLastUpdate())
                .name(todo.getName())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .build();
    }
}
