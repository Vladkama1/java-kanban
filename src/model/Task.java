package model;

import service.Type;

import java.time.LocalDateTime;

public class Task {
    protected Integer id = 0;
    protected Status status;
    protected String name;
    protected String description;
    protected int duration;
    protected LocalDateTime startTime;

    public Task(String name, String description) {
        status = Status.NEW;
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, int duration, LocalDateTime startTime) {
        status = Status.NEW;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public LocalDateTime getEndTime() {
        LocalDateTime endTime = null;
        if (startTime != null){
            endTime = startTime.plusMinutes(duration);
        }
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "\n" + id + "," + Type.TASK + "," + name + "," +
                status + "," + description + "," + startTime + "," + duration + ",";
    }
}