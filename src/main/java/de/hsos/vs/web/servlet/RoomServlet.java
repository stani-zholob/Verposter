package de.hsos.vs.web.servlet;


import com.google.gson.Gson;
import de.hsos.vs.web.entities.Lobby;
import de.hsos.vs.web.inmemory.Member;
import de.hsos.vs.web.inmemory.Room;
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
    Lobby lobby = new Lobby();
    ArrayList<Room> rooms = lobby.getRooms();

    @Override
    public void init() throws ServletException {

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

        // /api/rooms/1
        if (pathParts.length == 1) {
            resp.setContentType("application/json");
            Gson gson = new Gson();

            int roomId = getRoomIndex(req);
            Room currentRoom = rooms.get(roomId);
            resp.getWriter().write(gson.toJson(currentRoom));
            System.out.println(currentRoom);
        }


        // /api/rooms/1/members
        if (pathParts.length == 2 && pathParts[1].equals("members")) {
            int roomId = Integer.parseInt(pathParts[0]);
            HttpSession session = req.getSession(false);
            Integer userId = (Integer) session.getAttribute("userId");
            String username = (String) session.getAttribute("username");

            if (session == null || userId == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            //TESTS
            System.out.println(roomId + " " + userId + " " + username);
            ArrayList<Room> rooms = lobby.getRooms();
            Room currentRoom = rooms.get(roomId);

            List<Member> members = currentRoom.getMembers();


            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(members));
            resp.setStatus(HttpServletResponse.SC_OK);

            System.out.println(members);

            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] pathParts = getPathParts(req);

        //POST /api/rooms/*

        //POST /api/rooms/       JSON:{"name": name}
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

            Room room = lobby.getRoomById(roomId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(room));
            resp.sendRedirect("/Verposter/gamestart");

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
