package com.teachmeskills.hw.lesson_26.storage;

import com.teachmeskills.hw.lesson_26.model.Task;
import com.teachmeskills.hw.lesson_26.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class TaskStorage {
    public static Map<User, Set<Task>> taskDatabase = new HashMap<>();

    public TaskStorage() {
        taskDatabase = new HashMap<>();
    }

    public Map<User, Set<Task>> getTaskList() {
        return taskDatabase;
    }

    public static Set<Task> getTasksForUser(User user) {
        return taskDatabase.getOrDefault(user, new HashSet<>());
    }

    static {
        Set<Task> tasks = new HashSet<>();
        tasks.add(new Task(5, "Task 5", "Task 5"));
        taskDatabase.put(UserStorage.userDatabase.get("user1"), tasks);
    }
}
