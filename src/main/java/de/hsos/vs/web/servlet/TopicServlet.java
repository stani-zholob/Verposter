package de.hsos.vs.web.servlet;

import com.google.gson.Gson;
import de.hsos.vs.web.entities.Member;
import de.hsos.vs.web.entities.Room;
import de.hsos.vs.web.entities.Topic;
import de.hsos.vs.web.entities.Topics;
import de.hsos.vs.wordservice.db.TopicDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


@WebServlet("/api/topics/*")
public class TopicServlet extends HttpServlet {

    TopicDAO topicDAO = new TopicDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  ServletException, IOException {

        try {
            Topics topics = new Topics(topicDAO.findAll());
            Gson gson = new Gson();

            resp.getWriter().write(gson.toJson(topics.getTopics()));

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*

    // Hier wäre die Stelle wo man die Topics auswählbar machen würde allerdings sprengt das unsere zeitlichen Möglichkeiten

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] pathParts = getPathParts(req);

        // PATCH /api/rooms/{roomId}/topics/{topicId}
        if (pathParts.length == 3 && pathParts[1].equals("topics")) {
            int roomId = Integer.parseInt(pathParts[0]);
            int selectedTopicId = Integer.parseInt(pathParts[2]);

            HttpSession session = req.getSession(false);
            if (session == null) return;

            session.setAttribute();

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new Gson().toJson(room.getMembers()));
        }
        // PATCH /api/rooms/{roomId}/topics/{topicId}
    }
    private String[] getPathParts(HttpServletRequest req){
        String pathLine = req.getPathInfo();
        if (pathLine == null || pathLine.equals("/")) return new String[0];

        // "/1/members" wird ["1", "members"]
        return pathLine.substring(1).split("/");
    }


    private int getTopicIndex(HttpServletRequest request){
        String pathLine = request.getPathInfo(); // da liegt jetzt /42
        if (pathLine == null || pathLine.equals("/")) return -1; //  falls /api/users oder /api/users/

        try {
            String numberString = pathLine.replace("/", "");
            return Integer.parseInt(numberString);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }*/
}
