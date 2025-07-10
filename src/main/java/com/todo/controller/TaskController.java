package com.todo.controller;

import com.todo.DTO.TaskResponseDTO;
import com.todo.DTO.TaskUpdateRequestDTO;
import com.todo.DTO.UserProfileResponseDTO;
import com.todo.entity.Task;
import com.todo.entity.User;
import com.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @GetMapping("/alltasks")
    public ResponseEntity<List<TaskResponseDTO>> getAllTaskByUser(Authentication authentication) {
        List<Task> tasksraw;
        List<TaskResponseDTO> tasks=new ArrayList<>();
        try{
            User user = (User) authentication.getPrincipal();
            tasksraw=taskService.findByUsername(user.getUsername());
            for (Task task : tasksraw) {
                TaskResponseDTO dto = new TaskResponseDTO();
                dto.setId(task.getId());
                dto.setTitle(task.getTitle());
                dto.setDescription(task.getDescription());
                dto.setStart_time(task.getStart_time());
                dto.setPriority(task.getPriority());
                dto.setStatus(task.getStatus());
                tasks.add(dto);
            }
            return ResponseEntity.ok(tasks);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable int id){
        Task task=taskService.getById(id);
        TaskResponseDTO dto=new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStart_time(task.getStart_time());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable int id, @RequestBody TaskUpdateRequestDTO task){
        taskService.updateTask(id,task);
        return ResponseEntity.ok("Update task successful");
    }
    @PostMapping("/createnew")
    public ResponseEntity<String> createTask(@RequestBody TaskUpdateRequestDTO newtask, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            taskService.createTask(user.getUsername(), newtask);
            return ResponseEntity.ok("Create new task successful");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e+"");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id){
        
        return ResponseEntity.ok("Delete task successful");
    }
}
