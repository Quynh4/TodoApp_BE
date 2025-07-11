package com.todo.DTO;

import java.time.LocalDate;

public class TaskCountByDate {
    private LocalDate date;
    private long doneCount;
    private long inProgressCount;

    // Default constructor
    public TaskCountByDate() {
    }

    public TaskCountByDate(LocalDate date, long doneCount, long inProgressCount) {
        this.date = date;
        this.doneCount = doneCount;
        this.inProgressCount = inProgressCount;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(long doneCount) {
        this.doneCount = doneCount;
    }

    public long getInProgressCount() {
        return inProgressCount;
    }

    public void setInProgressCount(long inProgressCount) {
        this.inProgressCount = inProgressCount;
    }
}
