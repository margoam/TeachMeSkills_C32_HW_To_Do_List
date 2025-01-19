package com.teachmeskills.hw.lesson_26.servlet;

import com.teachmeskills.hw.lesson_26.logging.Logger;
import com.teachmeskills.hw.lesson_26.model.User;
import com.teachmeskills.hw.lesson_26.storage.UserStorage;
import com.teachmeskills.hw.lesson_26.util.UserValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User user = UserStorage.userDatabase.get(username);

        if (username == null || password == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username and password are required.");
            return;
        }

        if (UserValidator.validateUserName(username, password)) {
            req.getSession().setAttribute("username", user.getLogin());
            req.getSession().setAttribute("role", user.getRole());

            Logger.log("User logged in: " + user.getLogin() + " (Role: " + user.getRole() + ")");
            resp.getWriter().println("Hi, " + user.getLogin() + "! Your role is " + user.getRole());
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password.");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (UserValidator.validateUserName(username, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            req.getRequestDispatcher("/page/to-do.html").forward(req, resp);
        } else {
            req.getRequestDispatcher("page/login.html").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
