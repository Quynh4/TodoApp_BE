package com.todo.service;

import com.todo.DTO.TaskUpdateRequestDTO;
import com.todo.entity.Task;
import com.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    public List<Task> findByUsername(String username) {
        return taskRepository.findByUsername(username).orElseThrow();
    }
    public Task getById(int id) {
        return taskRepository.getById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }
    public void updateTask(int taskid, TaskUpdateRequestDTO updatetask){
        Task task = getById(taskid);
        task.setDescription(updatetask.getDescription());
        task.setPriority(updatetask.getPriority());
        task.setStatus(updatetask.getStatus());
        task.setTitle(updatetask.getTitle());
        task.setStart_time(updatetask.getStart_time());
        taskRepository.save(task);
    }

    public void createTask(String username, TaskUpdateRequestDTO task) {
        Task newTask= new Task(username,task.getTitle(),task.getDescription(),task.getStart_time(),task.getPriority());
        taskRepository.save(newTask);
    }
}
