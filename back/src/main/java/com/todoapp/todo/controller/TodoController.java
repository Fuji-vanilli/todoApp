package com.todoapp.todo.controller;

import com.todoapp.todo.dto.TodoRequest;
import com.todoapp.todo.utils.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public interface TodoController {
    @PostMapping("add")
    ResponseEntity<Response> add(@RequestBody TodoRequest request);
    @PutMapping("update")
    ResponseEntity<Response> update(@RequestBody TodoRequest request);
    @GetMapping("get/{name}")
    ResponseEntity<Response> get(@PathVariable String name);
    @GetMapping("get")
    ResponseEntity<Response> getWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );
    @PatchMapping("patch/{id}")
    ResponseEntity<Response> patch(@PathVariable Long id, @RequestBody Map<String, Boolean> completedStatus);
    @GetMapping("all")
    ResponseEntity<Response> all();
    @DeleteMapping("delete/{id}")
    ResponseEntity<Response> delete(@PathVariable Long id);
}
