package org.example.devsync1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.devsync1.entities.Tag;
import org.example.devsync1.entities.Task;
import org.example.devsync1.entities.User;
import org.example.devsync1.enums.Role;
import org.example.devsync1.scheduler.TaskStatusScheduler;
import org.example.devsync1.services.TagService;
import org.example.devsync1.services.TaskService;
import org.example.devsync1.services.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "TaskServlet", urlPatterns = {"/tasks"})
public class TaskServlet extends HttpServlet {

    private TaskService taskService;
    private UserService userService;
    private TagService tagService;
    private TaskStatusScheduler taskStatusScheduler;
    @Override
    public void init() throws ServletException {
        taskService = new TaskService();
        userService = new UserService();
        tagService = new TagService();
        taskStatusScheduler = new TaskStatusScheduler();
        taskStatusScheduler.startScheduler();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        User loggedUser = (User) req.getSession().getAttribute("loggedUser");

        try {
            if ("create".equals(action)) {
                createPage(req, resp);
                req.getRequestDispatcher("/WEB-INF/views/task/createTask.jsp").forward(req, resp);
            } else if ("delete".equals(action)) {
                delete(req, resp);
            } else if ("edit".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Task task = taskService.findById(id);
                req.setAttribute("task", task);
                createPage(req, resp);
                req.getRequestDispatcher("/WEB-INF/views/task/editTask.jsp").forward(req, resp);
            } else {
                if (loggedUser.getRole().equals(Role.USER)) {
                    List<Task> tasks = taskService.findByUserId(loggedUser.getId());
                    req.setAttribute("tasks", tasks);
                    req.getRequestDispatcher("/WEB-INF/views/user/userInformation.jsp").forward(req, resp);
                } else {
                    List<Task> tasks = taskService.findAll();
                    req.setAttribute("tasks", tasks);
                    req.getRequestDispatcher("/WEB-INF/views/task/tasks.jsp").forward(req, resp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("save".equals(action)) {
            createTask(req, resp);
        } else if ("update".equals(action)) {
            updateTask(req, resp);
        } else if ("updateStatus".equals(action)) {
            updateStatus(req, resp);
        }
    }
    private void updateStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long taskId = Long.parseLong(req.getParameter("task_id"));
            String status = req.getParameter("status");

            // Find the task by ID
            Task task = taskService.findById(taskId);

            // Update the status if task exists
            if (task != null) {
                task.setStatus(status);
                taskService.update(task);
            }

            // Redirect back to the task list
            resp.sendRedirect(req.getContextPath() + "/tasks");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the task status: " + e.getMessage());
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long taskId = Long.parseLong(req.getParameter("id"));
        Task task = taskService.findById(taskId);
        taskService.delete(task);
        resp.sendRedirect(req.getContextPath() + "/tasks");
    }

    private void createTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            LocalDate creationDate = LocalDate.parse(req.getParameter("createdate"));
            LocalDate dueDate = LocalDate.parse(req.getParameter("dueDate"));
            String status = req.getParameter("status");
            User createdBy = (User) req.getSession().getAttribute("loggedUser");
            Long assignedToId = Long.parseLong(req.getParameter("assignedTo"));
            String[] tagIds = req.getParameterValues("tags[]");

            StringBuilder errors = new StringBuilder();

            if (dueDate.isBefore(creationDate)) {
                errors.append("La tâche ne peut pas avoir une date limite antérieure à sa date de création. ");
            }
            if (creationDate.isBefore(LocalDate.now().plusDays(3))) {
                errors.append("Les tâches doivent être planifiées au moins 3 jours à l'avance. ");
            }
            if (!createdBy.getRole().equals(Role.MANAGER) && !assignedToId.equals(createdBy.getId())) {
                errors.append("Vous ne pouvez attribuer une tâche qu'à vous-même, sauf si vous êtes un manager. ");
            }

            if (errors.length() > 0) {
                req.setAttribute("error", errors.toString());
                createPage(req, resp);
                req.getRequestDispatcher("/WEB-INF/views/task/createTask.jsp").forward(req, resp);
                return;
            }

            Optional<User> assignedTo = userService.findById(assignedToId);

            List<Tag> tags = new ArrayList<>();
            if (tagIds != null) {
                for (String tagId : tagIds) {
                    Tag tag = tagService.findById(Long.parseLong(tagId));
                    tags.add(tag);
                }
            }

            Task task = new Task(title, description, creationDate, dueDate, status, tags, createdBy, assignedTo.get());
            task.setTags(tags);

            taskService.save(task);

            resp.sendRedirect(req.getContextPath() + "/tasks");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Une erreur s'est produite lors de la création de la tâche : " + e.getMessage());
            createPage(req, resp);
            req.getRequestDispatcher("/WEB-INF/views/task/createTask.jsp").forward(req, resp);
        }
    }

    private void createPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loggedUser = (User) req.getSession().getAttribute("loggedUser");
        if (loggedUser.getRole().equals(Role.MANAGER)) {
            List<User> users = userService.findAll();
            req.setAttribute("users", users);
        } else {
            req.setAttribute("users", List.of(loggedUser));
        }
        List<Tag> tags = tagService.findAll();
        req.setAttribute("tags", tags); //
    }

    private void updateTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long taskId = Long.parseLong(req.getParameter("id"));
            Task task = taskService.findById(taskId);
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            User createdBy = (User) req.getSession().getAttribute("loggedUser");
            LocalDate creationDate = LocalDate.parse(req.getParameter("createdate"));
            LocalDate dueDate = LocalDate.parse(req.getParameter("dueDate"));
            String status = req.getParameter("status");
            Long assignedToId = Long.parseLong(req.getParameter("assignedTo"));
            Optional<User> assignedTo = userService.findById(assignedToId);
            String[] tagIds = req.getParameterValues("tags[]");

            StringBuilder errors = new StringBuilder();

            if (dueDate.isBefore(creationDate)) {
                errors.append("The task cannot have a due date earlier than its creation date. ");
            }
            if (creationDate.isBefore(LocalDate.now().plusDays(3))) {
                errors.append("Tasks should be planned at least 3 days in advance.");
            }
            if (!createdBy.getRole().equals(Role.MANAGER) && !assignedToId.equals(createdBy.getId())) {
                errors.append("You can only assign a task to yourself, unless you are a manager.");
            }

            if (errors.length() > 0) {
                req.setAttribute("error", errors.toString());
                createPage(req, resp);
                req.getRequestDispatcher("/WEB-INF/views/task/editTask.jsp").forward(req, resp);
                return;
            }

            List<Tag> tags = new ArrayList<>();
            if (tagIds != null) {
                for (String tagId : tagIds) {
                    Tag tag = tagService.findById(Long.parseLong(tagId));
                    tags.add(tag);
                }
            }

            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);
            task.setStatus(status);
            task.setAssignedTo(assignedTo.get());
            task.setTags(tags);

            taskService.update(task);

            resp.sendRedirect(req.getContextPath() + "/tasks");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred while updating the task: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/task/editTask.jsp").forward(req, resp);
        }
    }
}
