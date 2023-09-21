package com.todoapp.todo.validator;


import com.todoapp.todo.dto.TodoRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TodoValidator {
    public static List<String> validate(TodoRequest request){
        List<String> errors= new ArrayList<>();

        if(Objects.isNull(request.getName())){
            errors.add("name required!");
        }
        if(Objects.isNull(request.getDescription())){
            errors.add("description required!");
        }
        return errors;
    }
}
