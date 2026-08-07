package de.hsos.vs.web.servlet;

import de.hsos.vs.web.entities.User;
import de.hsos.vs.wordservice.UserServiceImpl;
//import de.hsos.vs.wordservice.db.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/register")
public class RegsiterServlet extends HttpServlet {

    //UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //List<User> users = userService.findAllUsers();
        //req.setAttribute("users", users);
        req.getRequestDispatcher("/registration.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //User user = new User(username, password);
        //userDAO.insert(user);

        resp.sendRedirect("/login");
    }
}
