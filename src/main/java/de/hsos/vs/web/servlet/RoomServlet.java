package de.hsos.vs.web.servlet;


import com.google.gson.Gson;
import de.hsos.vs.entities.Lobby;
import de.hsos.vs.entities.Member;
import de.hsos.vs.entities.Room;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/rooms/*")
public class RoomServlet extends HttpServlet {
    private final Lobby lobby = new Lobby();


    /**
     * Verarbeitung der GET-Anfragen fuer die Raueme. Abhaengig von URL Pfad werden
     * - alle Raueme - ein bestimmter Raum - die Mitglider eines Raums
     * als JSON zurueckgegeben. Dafuer muss die Session schon existieren
     * @author Stanislav
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] pathParts = getPathParts(req);

        // GET /api/rooms
        if (pathParts.length == 0) {
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(new Gson().toJson(lobby.getRooms()));
            return;
        }

        // GET /api/rooms/1
        if (pathParts.length == 1) {
            int roomId = Integer.parseInt(pathParts[0]);

            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(new Gson().toJson(lobby.getRoomById(roomId)));
            return;
        }

        // /api/rooms/1/members
        if (pathParts.length == 2 && pathParts[1].equals("members")) {
            int roomId = Integer.parseInt(pathParts[0]);
            HttpSession session = req.getSession(false);

            if (session == null || session.getAttribute("userId") == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Room currentRoom = lobby.getRoomById(roomId);
            if (currentRoom == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            List<Member> members = currentRoom.getMembers();

            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(new Gson().toJson(members));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * @author Lukas, Stanislav
     *
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] pathParts = getPathParts(req);

        //POST /api/rooms/*

        //POST /api/rooms/       JSON:{"name": name}
        if (pathParts.length == 0) {
            Gson gson = new Gson();
            resp.setContentType("application/json");
            Room roomFromJson = gson.fromJson(req.getReader(), Room.class);
            Room newRoom = lobby.createRoom(roomFromJson.getName());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setHeader("Location", req.getContextPath() + "/api/rooms/" + newRoom.getId());
        }

        // POST /api/rooms/1/members
        if (pathParts.length == 2 && pathParts[1].equals("members")) {
            int roomId = Integer.parseInt(pathParts[0]);
            HttpSession session = req.getSession(false);

            if (session == null || session.getAttribute("userId") == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            int userId = (Integer) session.getAttribute("userId");
            String username = (String) session.getAttribute("username");

            Room room = lobby.getRoomById(roomId);

            if (room.getMemberById(userId) != null) {
                resp.sendRedirect(req.getContextPath() + "/gamestart?roomId=" + roomId);
                return;
            }

            room.addMember(new Member(userId, username));
            session.setAttribute("roomId", roomId);

            resp.sendRedirect(req.getContextPath() + "/gamestart?roomId=" + roomId);
        }
    }

    /**
     * schaltet den bereit-status des members um. das passiert mit patch weil das ist wie update bei CRUD
     * gibt eine ganze json zurück wo das bereit flag aktualisiert wurde
     *
     * @author Lukas
     */
    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] pathParts = getPathParts(req);

        // PATCH /api/rooms/1/members/ready
        if (pathParts.length == 3 && pathParts[1].equals("members") && pathParts[2].equals("ready")) {
            int roomId = Integer.parseInt(pathParts[0]);

            HttpSession session = req.getSession(false);
            if (session == null) return;
            int userId = (Integer) session.getAttribute("userId");

            Room room = lobby.getRoomById(roomId);
            if (room == null) return;
            Member member = room.getMemberById(userId);

            if (member == null) return;
            member.setReady(!member.isReady());

            session.setAttribute("ready", member.isReady());

            resp.setContentType("application/json;charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new Gson().toJson(room.getMembers()));
        }
    }

    /**
     * wird aufgerufen wenn verlassen button geklickt wird und holt userId um ihn aus dem Raum zu entfernen
     *
     * @author Lukas
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String[] pathParts = getPathParts(req);

        // DELETE /api/rooms/1/members/leave
        if (pathParts.length == 3 && pathParts[1].equals("members") && pathParts[2].equals("leave")) {
            int roomId = Integer.parseInt(pathParts[0]);

            HttpSession session = req.getSession(false);
            if (session == null) return;
            int userId = (Integer) session.getAttribute("userId");

            Room room = lobby.getRoomById(roomId);
            if (room == null) return;
            Member member = room.getMemberById(userId);

            if (member == null) return;

            room.removeMember(member);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    /**
     * @author Stanislav
     * req.getPathInfo() gibt /1/members fuer /api/rooms/1/members
     */
    private String[] getPathParts(HttpServletRequest req){
        String pathLine = req.getPathInfo();
        if (pathLine == null || pathLine.equals("/")) return new String[0];

        // "/1/members" wird ["1", "members"]
        return pathLine.substring(1).split("/");
    }
}
