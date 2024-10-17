package org.example.devsync1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.devsync1.entities.User;
import org.example.devsync1.enums.Role;
import org.example.devsync1.scheduler.TokenDeleteScheduler;
import org.example.devsync1.scheduler.TokenModifyScheduler;
import org.example.devsync1.services.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "UserServlet", urlPatterns = {"/users"})
public class UserServlet extends HttpServlet {

    private UserService userService;
    private TokenModifyScheduler tokenModifyScheduler;
    private TokenDeleteScheduler tokenDeleteScheduler;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        tokenModifyScheduler = new TokenModifyScheduler();
        tokenDeleteScheduler = new TokenDeleteScheduler();
        tokenModifyScheduler.startScheduler();
        tokenDeleteScheduler.startScheduler();
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
                case "login":
                    loginUser(req, res);
                    return;
                default:
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action specified.");
                    return;
            }
            res.sendRedirect("users");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    private void addUser(HttpServletRequest req) throws Exception {
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
        Optional<User> user = userService.findById(userId);
        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String roleStr = req.getParameter("role");
        Role role = Role.valueOf(roleStr.toUpperCase());
        user.get().setId(userId);
        user.get().setUsername(username);
        user.get().setFirstName(firstName);
        user.get().setLastName(lastName);
        user.get().setEmail(email);
        user.get().setPassword(hashedPassword);
        user.get().setRole(role);
        userService.update(user.get());
    }
    private void deleteUser(HttpServletRequest req , HttpServletResponse res) throws IOException {
        Long userId = Long.valueOf(req.getParameter("id"));
        Optional<User> user = userService.findById(userId);
        userService.delete(user.get());
        res.sendRedirect(req.getContextPath() + "/users");
    }
    private void loginUser(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            req.setAttribute("error", "User not found with this email");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, res);
            return;
        }

        User user = optionalUser.get(); // Retrieve the User object directly

        if (BCrypt.checkpw(password, user.getPassword())) {
            req.getSession().setAttribute("loggedUser", user); // Store User object directly in session
            if (user.getRole() == Role.MANAGER) {
                res.sendRedirect(req.getContextPath() + "/users");
            } else {
                res.sendRedirect(req.getContextPath() + "/tasks");
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
                Optional<User> user1 = userService.findById(userId);
                User user = user1.get();
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

