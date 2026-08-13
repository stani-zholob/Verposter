package de.hsos.vs.web.servlet;

import com.google.gson.Gson;
import de.hsos.vs.database.TopicDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * ein REST Endpoit zum Abrufen der Themen. Die Themen werden aus einer Datenbank gezogen und als JSON mit GET zurueckgegebn
 * @author Stanislav
 */
@WebServlet("/api/topics/*")
public class TopicServlet extends HttpServlet {
    TopicDAO topicDAO = new TopicDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json;charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new Gson().toJson(topicDAO.findAll()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
