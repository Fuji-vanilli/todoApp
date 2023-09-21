package com.todoapp.todo.repository;

import com.todoapp.todo.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByName(String name);
    Page<Todo> findAll(Pageable pageable);
    boolean existsByName(String name);
    void deleteByName(String name);
}
