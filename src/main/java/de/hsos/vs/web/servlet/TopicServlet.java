package de.hsos.vs.web.servlet;

import com.google.gson.Gson;
import de.hsos.vs.web.entities.Topic;
import de.hsos.vs.wordservice.db.TopicDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/api/topics/*")
public class TopicServlet extends HttpServlet {

    TopicDAO topicDAO = new TopicDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  ServletException, IOException {

        List<Topic> topics = null;
        try {
            topics = topicDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String path = req.getServletPath();
        for (int i = 0; topics.size() > i; i++) {

            Gson gson = new Gson();
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(topics.get(i)));
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);


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
    }
}
