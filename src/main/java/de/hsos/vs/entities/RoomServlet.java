package de.hsos.vs.entities;


import com.google.gson.Gson;
import de.hsos.vs.services.LobbyService;
import de.hsos.vs.web.entities.Room;
import de.hsos.vs.web.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author Stanislav
 */
@WebServlet("/api/rooms/*")
public class RoomServlet extends HttpServlet {
    LobbyService service = new LobbyService();
    ArrayList<Room> rooms = service.getRooms();

    @Override
    public void init() throws ServletException {
        rooms.add(new Room(0,"VS gang"));
        rooms.add(new Room(1,"loosers kommt vorbei"));
        rooms.add(new Room(2,"Keine Name"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        Gson gson = new Gson();

        int roomId = getRoomIndex(req);
        if  (roomId == -1) {
            resp.getWriter().write((gson.toJson(rooms)));
            return;
        }

        Room foundRoom = null;
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                foundRoom = room;
                break;
            }
        }
        resp.getWriter().write(gson.toJson(foundRoom));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();

        resp.setContentType("application/json");
        Room roomFromJson = gson.fromJson(req.getReader(), Room.class);
        Room newRoom = new Room(rooms.size() + 1, roomFromJson.getName());
        rooms.add(newRoom);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private int getRoomIndex(HttpServletRequest request){
        String pathLine = request.getPathInfo(); // da liegt jetzt /42
        if (pathLine == null || pathLine.equals("/")) return -1; //  falls /api/users oder /api/users/

        try {
            String numberString = pathLine.replace("/", "");
            return Integer.parseInt(numberString);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
