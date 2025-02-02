package com.teachmeskills.hw.lesson_26.servlet;

import com.teachmeskills.hw.lesson_26.model.Task;
import com.teachmeskills.hw.lesson_26.model.User;
import com.teachmeskills.hw.lesson_26.storage.TaskStorage;
import com.teachmeskills.hw.lesson_26.storage.UserStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

import com.google.gson.Gson;


@WebServlet("/to-do")
public class TodoServlet extends HttpServlet {
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login");
            return;
        }

        String username = (String) session.getAttribute("username");
        User user = UserStorage.userDatabase.get(username);

        Set<Task> tasks = TaskStorage.getTasksForUser(user);

        String tasksJson = new Gson().toJson(tasks);
        resp.setContentType("application/json");
        req.setAttribute("tasks", tasksJson);
        req.getRequestDispatcher("page/to-do.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login");
            return;
        }

        String username = (String) session.getAttribute("username");
        User user = UserStorage.userDatabase.get(username);

        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        Set<Task> tasks = TaskStorage.getTasksForUser(user);

        String taskTitle = req.getParameter("task");
        String taskDescription = req.getParameter("description");

        if (taskTitle != null && !taskTitle.isEmpty()) {
            int newId = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
            Task newTask = new Task(newId, taskTitle, taskDescription != null ? taskDescription : "");
            tasks.add(newTask);
            TaskStorage.taskDatabase.put(user, tasks);
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new Gson().toJson(tasks));
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        String username = (String) session.getAttribute("username");
        User user = UserStorage.userDatabase.get(username);
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        String deletedTaskIdStr = req.getParameter("deletedTask");
        if (deletedTaskIdStr == null || deletedTaskIdStr.isEmpty()) {
            sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing task ID");
            return;
        }

        int deletedTaskId;
        try {
            deletedTaskId = Integer.parseInt(deletedTaskIdStr);
        } catch (NumberFormatException e) {
            sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID");
            return;
        }

        Set<Task> tasks = TaskStorage.getTasksForUser(user);
        if (!tasks.removeIf(task -> task.getId() == deletedTaskId)) {
            sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Task not found");
            return;
        }

        TaskStorage.taskDatabase.put(user, tasks);

        sendJsonResponse(resp, HttpServletResponse.SC_OK, tasks);
    }

    private void sendJsonResponse(HttpServletResponse resp, int status, Object message) throws IOException {
        resp.setStatus(status);
        resp.getWriter().write(new Gson().toJson(message));
    }

}

