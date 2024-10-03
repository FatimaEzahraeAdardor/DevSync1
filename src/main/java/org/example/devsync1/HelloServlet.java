package org.example.devsync1;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.devsync1.entities.User;
import org.example.devsync1.enums.Role;
import org.example.devsync1.repositories.implementations.UserRepository;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() throws ServletException {
        message = "Hello World!";
        try {
            // Initialize the EntityManagerFactory
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myJPAUnit");
            message = "Connected to the database successfully!";
        } catch (Exception e) {
            // Handle the exception
            message = "Error connecting to the database: " + e.getMessage();
        }


        UserRepository userRepository = new UserRepository();
        User user = new User();
        user.setUsername("usr");
        user.setFirstName("Jack");
        user.setLastName("Smith");
        user.setEmail("jack@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        try {
            userRepository.save(user);
        }catch (Exception e) {
            message = "Error saving user: " + e.getMessage();
        }

//        User user = new User();
//        user.setUsername("user");
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setEmail("john.doe@example.com");
//        user.setPassword("password");
//        user.setRole(Role.USER);
//        user.setUsername("user");
//        try{
//            userRepository.save(user);
//        }catch (Exception e){
//            throw new ServletException(e);
//        }
//        try {
//            // Fetch the existing user (e.g., with ID 1)
//            User user = userRepository.findById(2L);
//            if (user != null) {
////                user.setUsername("admin"); // Update the username or any other field
////                userRepository.update(user);
//                userRepository.delete(user.getId());
//                message = "User deleted successfully!";
//            } else {
//                message = "User not found!";
//            }
//        } catch (Exception e) {
//            throw new ServletException(e);
//        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}