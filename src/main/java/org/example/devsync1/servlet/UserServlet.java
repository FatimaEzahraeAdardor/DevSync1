package org.example.devsync1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.devsync1.entities.User;
import org.example.devsync1.enums.Role;
import org.example.devsync1.services.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.List;

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
                    deleteUser(req,res);
                    break;
                case "login":
                    loginUser(req, res);  // Handle login here
                    return;
                case "logout":
                    logoutUser(req, res);  // Handle login here
                    return;
                default:
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action specified.");
                    return;
            }
            res.sendRedirect("users");
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logging framework
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    private void addUser(HttpServletRequest req) {
        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String roleStr = req.getParameter("role");
        Role role = Role.valueOf(roleStr.toUpperCase());
        User user = new User(username, firstName, lastName, email, hashedPassword, role);
        userService.save(user);
    }

    private void updateUser(HttpServletRequest req) {
        Long userId = Long.valueOf(req.getParameter("id"));
        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Role role = Role.valueOf(req.getParameter("role").toUpperCase());
        User user = new User(username, firstName, lastName, email, hashedPassword, role);
        user.setId(userId);
        userService.update(user);
    }
    private void deleteUser(HttpServletRequest req , HttpServletResponse res) throws IOException {
        Long userId = Long.valueOf(req.getParameter("id"));
        User user = userService.findById(userId);
        userService.delete(user);
        res.sendRedirect(req.getContextPath() + "/users");
    }
    private void loginUser(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = userService.findByEmail(email);

        if (user == null) {
            req.setAttribute("error", "User not found with this email");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, res);
            return;
        }
        if (BCrypt.checkpw(password, user.getPassword())) {
            req.getSession().setAttribute("loggedUser", user);

            if (user.getRole() == Role.MANAGER) {
                req.getSession().setAttribute("user", user);
                res.sendRedirect(req.getContextPath() + "/users");
            } else {
                req.getRequestDispatcher("/WEB-INF/views/user/userInformation.jsp").forward(req, res);
            }
        } else {
            req.setAttribute("error", "Password mismatch for user");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, res);
        }
    }
    private void logoutUser(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.getSession().removeAttribute("loggedUser");
        req.getRequestDispatcher("index.jsp").forward(req, res);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                req.getRequestDispatcher("/WEB-INF/views/user/createUser.jsp").forward(req, res);
            } else if ("edit".equals(action)) {
                Long userId = Long.valueOf(req.getParameter("id"));
                User user = userService.findById(userId);
                req.setAttribute("user", user);
                req.getRequestDispatcher("/WEB-INF/views/user/editUser.jsp").forward(req, res);

            } else if ("delete".equals(action)) {
                deleteUser(req, res);
            }else if ("login".equals(action)) {
                req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, res);
            } else if ("logout".equals(action)) {
                logoutUser(req, res);
            }else {
                List<User> users = userService.findAll();
                req.setAttribute("users", users);
                req.getRequestDispatcher("/WEB-INF/views/user/users.jsp").forward(req, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }


}

