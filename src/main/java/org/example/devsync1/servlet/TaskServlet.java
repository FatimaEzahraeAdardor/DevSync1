package org.example.devsync1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.devsync1.entities.*;
import org.example.devsync1.enums.RequestStatus;
import org.example.devsync1.enums.Role;
import org.example.devsync1.repositories.implementations.RequestRepository;
import org.example.devsync1.scheduler.TaskStatusScheduler;
import org.example.devsync1.services.TagService;
import org.example.devsync1.services.TaskService;
import org.example.devsync1.services.TokenService;
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
    private RequestRepository requestRepository = new RequestRepository();
    private TokenService tokenService;
    @Override
    public void init() throws ServletException {
        taskService = new TaskService();
        userService = new UserService();
        tagService = new TagService();
        taskStatusScheduler = new TaskStatusScheduler();
        taskStatusScheduler.startScheduler();
        tokenService = new TokenService();
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
                List<Task> tasks;
                if (loggedUser.getRole().equals(Role.USER)) {
                    tasks = taskService.findByUserId(loggedUser.getId());
                    req.setAttribute("loggedUser", loggedUser);
                } else {
                    tasks = taskService.findAll();
                }

                // Fetch pending modification requests for each task
                List<Request> pendingRequests = new ArrayList<>();
                for (Task task : tasks) {
                    Optional<Request> requestOptional = requestRepository.findByTaskId(task.getId());
                    requestOptional.ifPresent(pendingRequests::add);
                }
                req.setAttribute("pendingRequests", pendingRequests);
                req.setAttribute("tasks", tasks);
                req.getRequestDispatcher(loggedUser.getRole().equals(Role.USER) ? "/WEB-INF/views/user/userInformation.jsp" : "/WEB-INF/views/task/tasks.jsp").forward(req, resp);
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
        } else if ("requestModification".equals(action)){
            saveRequest(req, resp);
        }else if ("acceptRequest".equals(action)) {
        acceptRequest(req, resp);
    } else if ("rejectRequest".equals(action)) {
        rejectRequest(req, resp);
    }
    }
    private void acceptRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requestIdParam = req.getParameter("requestId");
            if (requestIdParam == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID is required.");
                return;
            }

            Long requestId = Long.parseLong(requestIdParam);
            Optional<Request> requestOptional = requestRepository.findById(requestId);
            if (requestOptional.isPresent()) {
                Request request = requestOptional.get();
                request.setStatus(RequestStatus.ACCEPTED);
                Optional<Token> token = tokenService.findByUserId(request.getUser().getId());
                token.ifPresent(token1 -> {
                    token1.setModifyTokenCount(token.get().getModifyTokenCount() - 1);
                    tokenService.update(token1);
                    requestRepository.update(request);
                });

                resp.sendRedirect(req.getContextPath() + "/tasks");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while accepting the request: " + e.getMessage());
        }
    }

    private void rejectRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requestIdParam = req.getParameter("requestId");
            if (requestIdParam == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID is required.");
                return;
            }
            Long requestId = Long.parseLong(requestIdParam);
            Optional<Request> requestOptional = requestRepository.findById(requestId);
            if (requestOptional.isPresent()) {
                Request request = requestOptional.get();
                request.setStatus(RequestStatus.REJECTED);
                    requestRepository.update(request);
                resp.sendRedirect(req.getContextPath() + "/tasks");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while rejecting the request: " + e.getMessage());
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
    private void saveRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Request tokenRequest = new Request();
        tokenRequest.setTask(taskService.findById(Long.parseLong(req.getParameter("taskId"))));
        Optional<User> userOptional = userService.findById(Long.parseLong(req.getParameter("user_id")));
        userOptional.ifPresent(tokenRequest::setUser);
        tokenRequest.setStatus(RequestStatus.PENDING);
        requestRepository.save(tokenRequest);
        req.getSession().setAttribute("tokenRequest", tokenRequest);
    }
}
