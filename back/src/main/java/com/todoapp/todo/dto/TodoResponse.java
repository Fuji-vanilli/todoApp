package com.todoapp.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TodoResponse {
    private Long id;
    private String name;
    private String description;
    private boolean completed= false;
    private Date createdDate;
    private Date lastUpdate;
}
