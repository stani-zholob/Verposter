package de.hsos.vs.web.servlet;

import com.google.gson.Gson;
//import de.hsos.vs.entities.UserServlet;
import de.hsos.vs.web.entities.User;
import de.hsos.vs.wordservice.UserServiceImpl;
//import de.hsos.vs.wordservice.db.UserDAO;
import de.hsos.vs.wordservice.db.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/register")
public class RegsiterServlet extends HttpServlet {


    UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        User userFromJson = gson.fromJson(req.getReader(), User.class);

        //hier hashen
        User newUser  = new User(0,userFromJson.getName(), userFromJson.getPasswordHash());

        try {
            if(userDAO.findByName(newUser.getName()).isPresent()){
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().println("Username already exists");
                return;
            }
            userDAO.insert(newUser.getName(), newUser.getPasswordHash());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().println("Successfully registered");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        resp.sendRedirect("/login");
    }
}
