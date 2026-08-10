package de.hsos.vs.entities;


import com.google.gson.Gson;
import de.hsos.vs.services.LobbyService;
import de.hsos.vs.web.inmemory.Member;
import de.hsos.vs.web.inmemory.Room;
import de.hsos.vs.web.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String[] pathParts = getPathParts(req);


        if (pathParts.length == 0) {

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
        if (pathParts.length == 2 && pathParts[1].equals("members")) {
            int roomId = Integer.parseInt(pathParts[0]);
            HttpSession session = req.getSession(false);
            Integer userId = (Integer) session.getAttribute("userId");

            if (session == null || userId == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            //TESTS
            System.out.println(roomId + " " + userId);
            ArrayList<Room> rooms = service.getRooms();
            Room currentRoom = rooms.get(roomId);

            List<Member> members = currentRoom.getMembers();

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(members));

            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] pathParts = getPathParts(req);

        //POST /api/rooms/*

        //POST /api/rooms/       JSON:{"name": name}if (g)
        if (pathParts.length == 0) {
            Gson gson = new Gson();
            resp.setContentType("application/json");
            Room roomFromJson = gson.fromJson(req.getReader(), Room.class);
            Room newRoom = new Room(rooms.size() + 1, roomFromJson.getName());
            rooms.add(newRoom);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setHeader("Location", req.getContextPath() + "/api/rooms/" + newRoom.getId());
        }

        // POST /api/rooms/1/members
        if (pathParts.length == 2 && pathParts[1].equals("members")) {
            int roomId = Integer.parseInt(pathParts[0]);
            HttpSession session = req.getSession(false);
            Integer userId = (Integer) session.getAttribute("userId");
            String username = (String) session.getAttribute("username");

            if (userId == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
            Member member = new Member(userId,username);
            rooms.get(roomId).addMember(member);

            //TESTS
            System.out.println(roomId + " " + userId);

            Room room = service.joinRoom(roomId, userId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(room));

            return;
        }
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

    /**
     * Stanislav
     * req.getPathInfo() gibt /1/members fuer /api/rooms/1/members
     */
    private String[] getPathParts(HttpServletRequest req){
        String pathLine = req.getPathInfo();
        if (pathLine == null || pathLine.equals("/")) return new String[0];

        // "/1/members" wird ["1", "members"]
        return pathLine.substring(1).split("/");
    }
}
