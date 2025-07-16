package com.todo.service;

import com.todo.DTO.TaskCountByDate;
import com.todo.DTO.TaskStartedCountByDateDTO;
import com.todo.DTO.TaskUpdateRequestDTO;
import com.todo.entity.Task;
import com.todo.enumconstant.Status;
import com.todo.repository.TaskRepository;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public void updateTask(int taskid, TaskUpdateRequestDTO updatetask) {

        Task task = getById(taskid);
        if(task.getStatus()==Status.Inprogress && updatetask.getStatus()==Status.Done) {
            task.setComplete_time(LocalDateTime.now());
        }
        if(task.getStatus()==Status.Done && updatetask.getStatus() == Status.Inprogress) {
            task.setComplete_time(null);
        }
        task.setDescription(updatetask.getDescription());
        task.setPriority(updatetask.getPriority());
        task.setStatus(updatetask.getStatus());
        task.setTitle(updatetask.getTitle());
        task.setStart_time(updatetask.getStart_time());
        taskRepository.save(task);
    }

    public void createTask(String username, TaskUpdateRequestDTO task) {
        Task newTask = new Task(username, task.getTitle(), task.getDescription(), task.getStart_time(),
                task.getPriority());
        taskRepository.save(newTask);
    }

    public int countCompletedTaskByUsername(String username) {
        // TODO Auto-generated method stub
        List<Task> tasks = taskRepository.findByUsername(username)
                .orElse(Collections.emptyList());
        int count = 0;
        for (Task task : tasks) {
            if (task.getStatus().equals(Status.Done)) {
                count++;
            }
        }
        return count;
    }

    public int countOpenTaskByUsername(String username) {
        // TODO Auto-generated method stub
        List<Task> tasks = taskRepository.findByUsername(username).orElseThrow();
        int count = 0;
        for (Task task : tasks) {
            if (task.getStatus().equals(Status.Inprogress)) {
                count++;
            }
        }
        return count;
    }

    // TODO Auto-generated method stub
    public TaskCountByDate countTaskStatusByDate(String username, LocalDate date) {
        List<Task> tasks = findByUsername(username);

        long done = tasks.stream()
                .filter(task -> task.getComplete_time() != null)
                .filter(task -> task.getComplete_time().toLocalDate().equals(date))
                .filter(task -> task.getStatus() == Status.Done)
                .count();

        long inProgress = tasks.stream()
                .filter(task -> task.getStatus() == Status.Inprogress)
                .filter(task -> task.getStart_time() != null && !task.getStart_time().toLocalDate().isAfter(date))
                .filter(task -> task.getComplete_time() == null || task.getComplete_time().toLocalDate().isAfter(date))
                .count();

        return new TaskCountByDate(date, done, inProgress);
    }

    public List<TaskCountByDate> getDailyTaskStatus(String username, int days) {
        List<Task> tasks = findByUsername(username);
        LocalDate today = LocalDate.now();
        List<TaskCountByDate> result = new ArrayList<>();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);

            long done = tasks.stream()
                    .filter(t -> t.getComplete_time() != null)
                    .filter(t -> t.getComplete_time().toLocalDate().equals(date))
                    .filter(t -> t.getStatus() == Status.Done)
                    .count();

            long inProgress = tasks.stream()
                    .filter(t -> t.getStatus() == Status.Inprogress)
                    .filter(t -> t.getStart_time() != null && !t.getStart_time().toLocalDate().isAfter(date))
                    .filter(t -> t.getComplete_time() == null || t.getComplete_time().toLocalDate().isAfter(date))
                    .count();

            result.add(new TaskCountByDate(date, done, inProgress));
        }
        return result;

    }

    public List<TaskStartedCountByDateDTO> getTasksCreatedByDate(String username, int days) {
        List<Task> tasks = findByUsername(username);
        LocalDate today = LocalDate.now();
        List<TaskStartedCountByDateDTO> result = new ArrayList<>();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);

            long started = tasks.stream()
                    .filter(task -> task.getStart_time() != null)
                    .filter(task -> task.getStart_time().toLocalDate().equals(date))
                    .count();

            result.add(new TaskStartedCountByDateDTO(date, started));
        }

        return result;
    }

}
