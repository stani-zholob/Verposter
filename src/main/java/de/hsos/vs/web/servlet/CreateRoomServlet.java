package de.hsos.vs.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
/**
 * Dieser Servlet macht ausschliesslich den Redirect auf die html Seite durch GET createroom.html
 * WEnn die Session nicht existiert, wird Redirect auf den /login Servlet durchgefuehrt
 * Das funktioniert über ServletFilter (hat das Priorität)
 *
 * @author Stanislav
 */
@WebServlet("/createroom")
public class CreateRoomServlet extends HttpServlet {
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
