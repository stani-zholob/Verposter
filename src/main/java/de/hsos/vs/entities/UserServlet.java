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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * @author Stanislav
 */
@WebServlet("/api/users/*")
public class UserServlet extends HttpServlet {
    //UserDao userDao = new UserDao();
    LobbyService service = new LobbyService();
    ArrayList<User> users = service.getUsers();

    @Override
    public void init() throws ServletException {
        users.add(new User(1,"admin", "123"));
        users.add(new User(2,"new", "1235"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /**
         * die Daten werden wie ein JSON packet geschickt, Authentication teil ist bei ServletFilter
         * Stanislav
         */
        resp.setContentType("application/json");
        Gson gson = new Gson();

        //falls Verposter/users/ endpoint ist
        int userId = getUserIndex(req);
        if  (userId == -1) {
            resp.getWriter().write((gson.toJson(users) + "\n"));
            return;
        }
        //falls Verposter/users/5 bestimmter endpoint ist
        User foundUser = null;
        for (User user : users) {
            if (user.getId() == userId) {
                foundUser = user;
                break;
            }
        }
        resp.getWriter().write(gson.toJson(foundUser));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        User userFromJson = gson.fromJson(req.getReader(), User.class);

        User newUser = new User(users.size() + 1, userFromJson.getName(), userFromJson.getPasswordHash());
        users.add(newUser);
        resp.setStatus(HttpServletResponse.SC_OK);

//        PrintWriter out = resp.getWriter();
//        out.write(gson.toJson(users));
    }

    protected int getUserIndex(HttpServletRequest request){
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
