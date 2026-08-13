package de.hsos.vs.web.servlet;

import com.google.gson.Gson;
import de.hsos.vs.entities.User;
import de.hsos.vs.database.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Verwaltet die Registrierung neuer Nutzer
 * Falls der Name in einer Datenbank nicht existiert, wird der neue Nutzer mithilfe von userDAO gesprichert (insert)
 * Daten werden als JSON gesendet
 *
 * @author Stanislav
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {


    UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        User userFromJson = gson.fromJson(req.getReader(), User.class);
        User newUser  = new User(0,userFromJson.getName(), userFromJson.getPassword());

        try {
            if(userDAO.findByName(newUser.getName()).isPresent()){
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().println("Benutzername existiert bereits");
                return;
            }
            userDAO.insert(newUser.getName(), newUser.getPassword());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().println("Erfolgreich registriert");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // kein Redirect: register.html leitet bei response.ok selbst auf /login weiter
    }
}
