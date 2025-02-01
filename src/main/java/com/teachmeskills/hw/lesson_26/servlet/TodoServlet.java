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
            resp.sendRedirect("login.html");
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
            resp.sendRedirect("login.html");
            return;
        }

        String username = (String) session.getAttribute("username");
        User user = UserStorage.userDatabase.get(username);

        if (user == null) {
            resp.sendRedirect("login.html");
            return;
        }

        Set<Task> tasks = TaskStorage.getTasksForUser(user);

        String taskTitle = req.getParameter("task");

        if (taskTitle != null && !taskTitle.isEmpty()) {
            int newId = tasks.size() + 1;
            Task newTask = new Task(newId, taskTitle, "");
            tasks.add(newTask);
            TaskStorage.taskDatabase.put(user, tasks);
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new Gson().toJson(tasks));

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        String username = (String) session.getAttribute("username");
        User user = UserStorage.userDatabase.get(username);

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        String deletedTaskIdStr = req.getParameter("deletedTask");
        if (deletedTaskIdStr == null || deletedTaskIdStr.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Missing task ID\"}");
            return;
        }

        int deletedTaskId;
        try {
            deletedTaskId = Integer.parseInt(deletedTaskIdStr);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Invalid task ID\"}");
            return;
        }

        Set<Task> tasks = TaskStorage.getTasksForUser(user);
        boolean removed = tasks.removeIf(task -> task.getId() == deletedTaskId);

        if (!removed) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\": \"Task not found\"}");
            return;
        }

        TaskStorage.taskDatabase.put(user, tasks);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new Gson().toJson(tasks));
    }

}

