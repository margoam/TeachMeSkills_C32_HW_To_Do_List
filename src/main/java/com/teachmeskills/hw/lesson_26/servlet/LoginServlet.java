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

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

            //Logger.log("User logged in: " + user.getLogin() + " (Role: " + user.getRole() + ")");
            resp.sendRedirect("/to-do");
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password.");
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = (String) req.getSession().getAttribute("username");
        if (username == null) {
            req.getRequestDispatcher("page/login.html").forward(req, resp);
            resp.getWriter().println("<h2>Login Failed. Please, sign up</h2>");
        } else {
            resp.sendRedirect("/to-do");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
