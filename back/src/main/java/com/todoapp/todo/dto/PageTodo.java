package com.todoapp.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PageTodo {
    private int number;
    private int size;
    private int totalElements;
    private int totalPages;
}
