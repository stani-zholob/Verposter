package de.hsos.vs.web.servlet;

import de.hsos.vs.web.entities.Lobby;
import de.hsos.vs.web.entities.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/createroom")
public class CreateRoomServlet extends HttpServlet {
    Lobby service = new Lobby();
    ArrayList<Room> rooms = service.getRooms();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/createroom.html").forward(req, resp);
    }

}
