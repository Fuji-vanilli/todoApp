package com.todoapp.todo.controller;

import com.todoapp.todo.dto.TodoRequest;
import com.todoapp.todo.service.TodoService;
import com.todoapp.todo.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.todoapp.todo.utils.Root.APP_ROOT;

@RestController
@RequestMapping(APP_ROOT)
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TodoApi implements TodoController{
    private final TodoService todoService;
    @Override
    public ResponseEntity<Response> add(TodoRequest request) {
        return ResponseEntity.ok(todoService.add(request));
    }

    @Override
    public ResponseEntity<Response> update(TodoRequest request) {
        return ResponseEntity.ok(todoService.update(request));
    }

    @Override
    public ResponseEntity<Response> get(String name) {
        return ResponseEntity.ok(todoService.get(name));
    }

    @Override
    public ResponseEntity<Response> getWithPagination(int page, int size) {
        return ResponseEntity.ok(todoService.getWithPagination(page, size));
    }

    @Override
    public ResponseEntity<Response> patch(Long id, Map<String, Boolean> completedStatus) {
        return ResponseEntity.ok(todoService.patch(id, completedStatus));
    }

    @Override
    public ResponseEntity<Response> all() {
        return ResponseEntity.ok(todoService.all());
    }

    @Override
    public ResponseEntity<Response> delete(Long id) {
        return ResponseEntity.ok(todoService.delete(id));
    }
}
