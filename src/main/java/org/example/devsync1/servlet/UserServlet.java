package org.example.devsync1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.devsync1.entities.User;
import org.example.devsync1.enums.Role;
import org.example.devsync1.services.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "UserServlet", urlPatterns = {"/users"})
public class UserServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "add":
                    addUser(req);
                    break;
                case "update":
                    updateUser(req);
                    break;
                case "delete":
                    deleteUser(req);
                    break;
                default:
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action specified.");
                    return;
            }
            res.sendRedirect("users");
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    private void addUser(HttpServletRequest req) {
        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String roleStr = req.getParameter("role");

        Role role = Role.valueOf(roleStr.toUpperCase());
        User user = new User(username, firstName, lastName, email, password, role);
        userService.save(user);

    }

    private void updateUser(HttpServletRequest req) {
        Long userId = Long.valueOf(req.getParameter("id"));
        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Role role = Role.valueOf(req.getParameter("role").toUpperCase());

        User user = new User(username, firstName, lastName, email, password, role);
        user.setId(userId);
        userService.update(user);
    }

    private void deleteUser(HttpServletRequest req) {
        Long userId = Long.valueOf(req.getParameter("id"));
        Optional<User> user = userService.findById(userId);
        user.ifPresent(userService::delete);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                req.getRequestDispatcher("/createUser.jsp").forward(req, res);
            } else if ("edit".equals(action)) {
                Long userId = Long.valueOf(req.getParameter("id"));
                Optional<User> user = userService.findById(userId);
                req.setAttribute("user", user.orElse(null));
                req.getRequestDispatcher("/editUser.jsp").forward(req, res);
            } else {
                List<User> users = userService.findAll();
                req.setAttribute("users", users);
                req.getRequestDispatcher("/users.jsp").forward(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

}
