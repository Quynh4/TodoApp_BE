package com.todo.entity;

import com.todo.enumconstant.Priority;
import com.todo.enumconstant.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
@Entity
@Table(name="task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String username;
    @NotBlank
    private String title;
    private String description;
    private LocalDateTime start_time;
    private Priority priority;
    private Status status;
    private LocalDateTime complete_time;
    public Task() {
    }

    public Task(String username, String title, String description, LocalDateTime start_time, Priority priority) {
        this.username = username;
        this.title = title;
        this.description = description;
        this.start_time = start_time;
        if(this.priority==null) {
            this.priority=Priority.Low;
        }
        else {
            this.priority = priority;
        }
        this.status = Status.Inprogress;
    }

    public int getId() {
        return id;
    }


    public LocalDateTime getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(LocalDateTime complete_time) {
        this.complete_time = complete_time;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
