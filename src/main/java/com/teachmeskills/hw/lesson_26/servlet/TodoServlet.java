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


@WebServlet("/to-do")
public class TodoServlet extends HttpServlet {

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

        req.setAttribute("tasks", tasks);
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
        String deletedTask = req.getParameter("deletedTask");

        if (taskTitle != null && !taskTitle.isEmpty()) {
            int newId = tasks.size() + 1;
            Task newTask = new Task(newId, taskTitle, "");
            tasks.add(newTask);
            TaskStorage.taskDatabase.put(user, tasks);
        }

        if (deletedTask != null) {
            tasks.removeIf(task -> task.getTitle().equals(deletedTask));
            TaskStorage.taskDatabase.put(user, tasks);
        }

        req.setAttribute("tasks", tasks);
        resp.sendRedirect("to-do");
    }

}
