package com.todo.DTO;

import java.time.LocalDate;

public class TaskStartedCountByDateDTO {
    private LocalDate date;
    private long startedCount;

    public TaskStartedCountByDateDTO() {
    }

    public TaskStartedCountByDateDTO(LocalDate date, long startedCount) {
        this.date = date;
        this.startedCount = startedCount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getstartedCount() {
        return startedCount;
    }

    public void setstartedCount(long startedCount) {
        this.startedCount = startedCount;
    }
}
