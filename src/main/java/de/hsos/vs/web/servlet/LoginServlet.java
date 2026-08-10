package de.hsos.vs.web.servlet;

import com.google.gson.Gson;
import de.hsos.vs.web.entities.User;
import de.hsos.vs.web.entities.Member;
import de.hsos.vs.wordservice.db.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        User userFromJson = gson.fromJson(req.getReader(), User.class);


        User newUser  = new User(0,userFromJson.getName(), userFromJson.getPassword());


        //wir pruefen ob die Daten Nullen sind
        if (newUser.getName() == null || newUser.getPassword() == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
        }

        //test ausgabe
        System.out.println("username = " + newUser.getName());
        System.out.println("password = " + newUser.getPassword());

        try {
            // in einer Datenbank suchen also ein Objekt aus einer Datebank
            User user = userDAO.findByName(newUser.getName()).orElse(null);
            Member member = new Member(user.getId(), user.getName());


            //falls es nicht in der Tabelle oder den Passwort nicht stimmt, dann redirect zurück
            if (!newUser.getPassword().equals(user.getPassword())) {
                resp.sendRedirect(req.getContextPath() + "/login.html");
            }

            //neue Session erzeuen
            HttpSession session = req.getSession();
            session.setAttribute("userId", member.getId());
            session.setAttribute("username", member.getName());
            session.setAttribute("isReady", member.isReady());

            //redirect weiter
            resp.sendRedirect(req.getContextPath() + "/lobby");


        } catch (SQLException e) {

            throw new RuntimeException(e);
        }


        /*

        if ("chef".equals(username) && "123".equals(password)) {

            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getId);
            session.setAttribute("username", username);
            resp.sendRedirect(req.getContextPath() + "/lobby");

        }
        else {
            PrintWriter out = resp.getWriter();
            out.println("Wrong Password");
            resp.sendRedirect(req.getContextPath() + "/login");
        }

         */
    }
}
