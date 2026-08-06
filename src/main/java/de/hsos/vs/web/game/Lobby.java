package de.hsos.vs.web.game;

import de.hsos.vs.web.entities.Room;
import de.hsos.vs.web.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Lukas
 */
@WebServlet("/lobby")
public class Lobby extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.sendRedirect("lobby.html");
        User u = new User();
        Room room = new Room(1);
        room.addUser(u);
    }
}