package de.hsos.vs.web.servlet;

import com.google.gson.Gson;
import de.hsos.vs.entities.User;
import de.hsos.vs.database.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * todo doku
 *
 * @author Stanislav
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    UserDAO userDAO = new UserDAO();


    public LoginServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            resp.sendRedirect(req.getContextPath() + "/lobby");
        } else{
            req.getRequestDispatcher("/login.html").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Gson gson = new Gson();
        User userFromJson = gson.fromJson(req.getReader(), User.class);


        User newUser  = new User(0,userFromJson.getName(), userFromJson.getPassword());


        //wir pruefen ob die Daten Nullen sind
        if (newUser.getName() == null || newUser.getPassword() == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        //test ausgabe
        System.out.println("username = " + newUser.getName());
        System.out.println("password = " + newUser.getPassword());

        try {
            // in einer Datenbank suchen also ein Objekt aus einer Datebank
            User user = userDAO.findByName(userFromJson.getName()).orElse(null);
            if (user == null || !user.getPassword().equals(userFromJson.getPassword())) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getName());
            session.setAttribute("ready", false);
            resp.setStatus(HttpServletResponse.SC_OK);

            //redirect weiter
            resp.sendRedirect(req.getContextPath() + "/lobby");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
