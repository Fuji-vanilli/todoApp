package com.todoapp.todo.service;

import com.todoapp.todo.dto.TodoRequest;
import com.todoapp.todo.utils.Response;

import java.util.Map;

public interface TodoService {
    Response add(TodoRequest request);
    Response update(TodoRequest request);
    Response get(String name);
    Response getWithPagination(int page, int size);
    Response patch(Long id, Map<String, Boolean> completedStatus);

    Response all();
    Response delete(Long id);
}
