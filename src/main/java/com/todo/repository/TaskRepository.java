package com.todo.repository;

import com.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<List<Task>> findByUsername(String username);
    Optional<Task> getById(int id);
}
