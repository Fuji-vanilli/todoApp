package com.todoapp.todo.service;

import com.todoapp.todo.dto.PageTodo;
import com.todoapp.todo.dto.TodoMapper;
import com.todoapp.todo.dto.TodoRequest;
import com.todoapp.todo.model.Todo;
import com.todoapp.todo.repository.TodoRepository;
import com.todoapp.todo.utils.Response;
import com.todoapp.todo.validator.TodoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TodoServiceImpl implements TodoService{
    private final TodoRepository repository;
    private final TodoMapper mapper;
    @Override
    public Response add(TodoRequest request) {
        List<String> errors= TodoValidator.validate(request);
        if(!errors.isEmpty()){
            log.error("some field is wrong...Please try again");
            return generateResponse(
                    HttpStatus.BAD_REQUEST,
                    null,
                    null,
                    "some field is wrong...Please try again"
            );
        }
        if(repository.existsByName(request.getName())){
            log.error("todo already exist...Please try again");
            return generateResponse(
                    HttpStatus.CONFLICT,
                    null,
                    null,
                    "todo already exist...Please try again"
            );
        }
        Todo todo= mapper.mapToTodo(request);
        todo.setCreatedDate(new Date());
        todo.setLastUpdate(new Date());

        repository.save(todo);

        URI location= ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("{name}")
                .buildAndExpand("api/todo/get/"+todo.getName())
                .toUri();

        return generateResponse(
                HttpStatus.OK,
                location,
                Map.of(
                        "todo", mapper.mapToTodoResponse(todo)
                ),
                "new todo added successfully!"
        );
    }

    @Override
    public Response update(TodoRequest request) {
        if(!repository.existsByName(request.getName())){
            log.error("sorry the name doesn't exist on the database");
            return generateResponse(
                    HttpStatus.BAD_REQUEST,
                    null,
                    null,
                    "sorry the name doesn't exist on the database"
            );
        }
        Todo todo= repository.findByName(request.getName()).orElse(null);
        assert todo != null;
        todo.setDescription(request.getDescription());
        repository.save(todo);

        URI location= ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("{name}")
                .buildAndExpand("api/todo/get/"+todo.getName())
                .toUri();
        log.info("todo with the name: {} updated successfully", request.getName());
        return generateResponse(
                HttpStatus.OK,
                location,
                Map.of(
                        "todo", mapper.mapToTodoResponse(todo)
                ),
                "todo with the name: "+request.getName()+" updated successfully!"

        );
    }
    @Override
    public Response get(String name) {
        if(!repository.existsByName(name)){
            log.error("sorry the todo doesn't exist on the database");
            return generateResponse(
                    HttpStatus.BAD_REQUEST,
                    null,
                    null,
                    "sorry the todo doesn't exist on the database"
            );
        }
        log.info("todo with the name: {} getted successfully!", name);
        return generateResponse(
                HttpStatus.OK,
                null,
                Map.of(
                        "todo", mapper.mapToTodoResponse(repository.findByName(name).orElse(null))
                ),
                "todo with the name: "+name+" getted successfully!"
        );
    }

    @Override
    public Response getWithPagination(int page, int size) {
        Pageable pageable= PageRequest.of(page, size);
        final Page<Todo> todosPagination = repository.findAll(pageable);
        final int totalPages = todosPagination.getTotalPages();
        log.info("todos with the pagination getted successfully");
        return generateResponse(
                HttpStatus.OK,
                null,
                Map.of(
                        "todos", todosPagination.getContent().stream()
                                .map(mapper::mapToTodoResponse)
                                .toList(),
                        "page", PageTodo.builder()
                                        .number(todosPagination.getNumber())
                                        .size(todosPagination.getSize())
                                        .totalElements(todosPagination.getNumberOfElements())
                                        .totalPages(todosPagination.getTotalPages())
                                .build()
                ),
                "todos with the pagination getted successfully"
        );
    }

    @Override
    public Response patch(Long id, Map<String, Boolean> completedStatus) {
        Todo todo= repository.findById(id).orElse(null);

        assert todo != null;
        todo.setCompleted(completedStatus.get("completed"));
        log.info("todo with the id: "+id+" patched successfully!");
        return generateResponse(
                HttpStatus.OK,
                null,
                Map.of(
                        "todo", mapper.mapToTodoResponse(todo)
                ),
                "todo with the id: "+id+" patched successfully!"
        );
    }

    @Override
    public Response all() {
        log.info("all todo getted successfully!");
        return generateResponse(
                HttpStatus.OK,
                null,
                Map.of(
                        "todos", repository.findAll().stream()
                                .map(mapper::mapToTodoResponse)
                                .toList()
                ),
                "all todo getted successfully!"
        );
    }

    @Override
    public Response delete(Long id) {
        Optional<Todo> todoOptional= repository.findById(id);
        if(todoOptional.isEmpty()){
            log.error("sorry the todo doesn't exist on the database");
            return generateResponse(
                    HttpStatus.BAD_REQUEST,
                    null,
                    null,
                    "sorry the todo doesn't exist on the database"
            );
        }
        repository.deleteById(id);
        log.info("todo with the id: {} is deleted successfully!", id);
        return generateResponse(
                HttpStatus.OK,
                null,
                null,
                "todo with the id: "+id+" deleted successfully!"
        );
    }
    private Response generateResponse(HttpStatus status, URI location, Map<?, ?> data, String message){
        return Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(status)
                .statusCode(status.value())
                .location(location)
                .data(data)
                .message(message)
                .build();
    }
}
