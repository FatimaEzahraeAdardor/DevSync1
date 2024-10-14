<%@ page import="org.example.devsync1.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.devsync1.entities.Task" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Task Management Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        @media (max-width: 640px) {
            .table-responsive {
                overflow-x: auto;
            }
        }
    </style>
</head>
<body class="bg-gray-100 text-gray-900 min-h-screen flex flex-col">

<!-- Navbar -->
<nav class="bg-blue-600 p-4 shadow-lg">
    <div class="container mx-auto flex justify-between items-center">
        <h1 class="text-white text-3xl font-bold">Dashboard</h1>
    </div>
</nav>

<!-- Main layout -->
<div class="flex flex-grow">
    <aside class="bg-white w-64 p-6 shadow-lg">
    <h2 class="text-xl font-bold mb-4">Menu</h2>
    <ul>
        <li class="mb-2">
            <a href="tasks" class="block p-2 hover:bg-blue-200 rounded">Tasks</a>
        </li>
        <li>
            <a href="users?action=logout" class="block p-2 hover:bg-blue-200 rounded">Logout</a>
        </li>
    </ul>
</aside>

    <!-- Main content -->
    <div class="container mx-auto mt-8 flex-grow p-6 bg-white rounded-lg shadow-lg">
        <%
            // Retrieve users from request attribute
            List<Task> tasks = (List<Task>) request.getAttribute("tasks");
        %>

        <div class="flex justify-between items-center mb-4">
            <h1 class="text-3xl font-bold">Task Management</h1>
            <a href="tasks?action=create" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5">Add Task</a>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <!-- Example Card for Users -->
            <div class="bg-white p-4 rounded-lg shadow">
                <h2 class="font-bold text-xl mb-2">Total Tasks</h2>
                <p class="text-3xl"><%= tasks != null ? tasks.size() : 0 %></p>
            </div>
        </div>

        <div class="table-responsive">
            <table class="min-w-full bg-white shadow-md rounded-lg overflow-hidden">
                <thead>
                <tr class="border-b bg-gray-200">
                    <th class="text-left p-3 px-5">ID</th>
                    <th class="text-left p-3 px-5">Created At</th>
                    <th class="text-left p-3 px-5">Due Date</th>
                    <th class="text-left p-3 px-5">Status</th>
                    <th class="text-left p-3 px-5">Tags</th>
                    <th class="text-left p-3 px-5">Created By</th>
                    <th class="text-left p-3 px-5">Assigned To</th>
                    <th class="text-left p-3 px-5">Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (tasks != null && !tasks.isEmpty()) {
                        for ( Task task : tasks) {
                %>
                <tr class="border-b hover:bg-orange-100 transition-colors duration-200">
                    <td class="p-3 px-5"><%= task.getId()!= null ? task.getId() : "N/A" %></td>
                    <td class="p-3 px-5"><%= task.getCreationDate()!= null ? task.getCreationDate() : "N/A" %></td>
                    <td class="p-3 px-5"><%= task.getDueDate()!= null ? task.getDueDate() : "N/A" %></td>
                    <td class="p-3 px-5"><%= task.getStatus()!= null ? task.getStatus() : "N/A" %></td>
                    <td class="p-3 px-5">
                        <%= task.getTags() != null && !task.getTags().isEmpty() ?
                                task.getTags().stream().map(Object::toString).collect(Collectors.joining(", ")) :
                                "N/A" %>
                    </td>

                    <td class="p-3 px-5"><%= task.getUser().getUsername()!= null ? task.getUser().getUsername() : "N/A" %></td>
                    <td class="p-3 px-5"><%= task.getAssignedTo().getUsername()!= null ? task.getAssignedTo().getUsername() : "N/A" %></td>
                    <td class="p-3 px-5 flex space-x-2">
                        <%
                            User loggedUser = (User) request.getAttribute("loggedUser");
                            if (loggedUser != null) {
                                if (task.getUser() != null && task.getUser().getId().equals(loggedUser.getId())) {
                        %>
                        <a href="tasks?action=edit&id=<%= task.getId() %>" class="px-4 py-2 bg-green-500 hover:bg-green-600 text-white rounded-lg transition duration-200">Edit</a>
                        <a href="tasks?action=delete&id=<%= task.getId() %>" class="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg transition duration-200">Delete</a>
                        <%
                        } else {
                        %>
                        <form action="tasks?action=requestModification" method="post">
                            <input type="hidden" name="taskId" value="<%= task.getId() %>">
                            <input type="hidden" name="user_id" value="<%= task.getAssignedTo().getId() %>">
                            <button type="submit" class="px-4 py-2 bg-yellow-500 hover:bg-yellow-600 text-white rounded-lg">Request Modification</button>
                        </form>
                        <form action="tasks?action=requestDeletion" method="post">
                            <input type="hidden" name="taskId" value="<%= task.getId() %>">
                            <input type="hidden" name="user_id" value="<%= task.getAssignedTo().getId() %>">
                            <button type="submit" class="px-4 py-2 bg-yellow-500 hover:bg-yellow-600 text-white rounded-lg">Request Deletion</button>
                        </form>
                        <%
                                }
                            }
                        %>

                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="7" class="p-3 text-center">No users found.</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="bg-gray-800 p-4 mt-auto">
    <div class="container mx-auto text-center text-white">
        <p>&copy; 2024 User Management System. All rights reserved.</p>
    </div>
</footer>

</body>
</html>
