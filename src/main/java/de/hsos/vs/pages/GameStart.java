package de.hsos.vs.pages;

import de.hsos.vs.entities.Room;
import de.hsos.vs.entities.User;
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
@WebServlet("/gamestart")
public class GameStart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.sendRedirect("gamestart.html");
        User u = new User();
        Room room = new Room(1);
        room.addUser(u);
    }
}