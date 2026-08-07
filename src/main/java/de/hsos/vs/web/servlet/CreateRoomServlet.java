package de.hsos.vs.web.servlet;

import de.hsos.vs.services.LobbyService;
import de.hsos.vs.web.entities.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/createroom")
public class CreateRoomServlet extends HttpServlet {
    LobbyService service = new LobbyService();
    ArrayList<Room> rooms = service.getRooms();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/createroom.html").forward(req,resp);
    }

}
