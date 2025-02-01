package com.teachmeskills.hw.lesson_26.model;

public class Task {
    private final int id;
    private final String title;
    private final String description;

    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
}
