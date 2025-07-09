package com.todo.DTO;

import com.todo.enumconstant.Priority;
import com.todo.enumconstant.Status;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class TaskUpdateRequestDTO {
    @NotBlank
    private String title;
    private String description;
    private LocalDateTime start_time;
    private Priority priority;
    private Status status;

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
