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
    <script>
        // Auto-submit form on dropdown change
        function submitFormOnChange(selectElement) {
            selectElement.form.submit();
        }
    </script>
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
    <!-- Include the sidebar -->
    <jsp:include page="../layout/side_bar.jsp" />

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

                    <!-- Add form to update status -->
                    <td class="p-3 px-5">
                        <form action="tasks?action=updateStatus" method="post">
                            <input type="hidden" name="task_id" value="<%= task.getId() %>">
                            <select name="status" class="px-2 py-1 border rounded-lg" onchange="submitFormOnChange(this)">
                                <option value="Pending" <%= "Pending".equals(task.getStatus()) ? "selected" : "" %>>Pending</option>
                                <option value="In Progress" <%= "In Progress".equals(task.getStatus()) ? "selected" : "" %>>In Progress</option>
                                <option value="Completed" <%= "Completed".equals(task.getStatus()) ? "selected" : "" %>>Completed</option>
                                <option value="Canceled" <%= "Canceled".equals(task.getStatus()) ? "selected" : "" %>>Canceled</option>
                            </select>
                        </form>
                    </td>

                    <td class="p-3 px-5">
                        <%= task.getTags() != null && !task.getTags().isEmpty() ?
                                task.getTags().stream().map(Object::toString).collect(Collectors.joining(", ")) :
                                "N/A" %>
                    </td>
                    <td class="p-3 px-5"><%= task.getUser().getUsername()!= null ? task.getUser().getUsername() : "N/A" %></td>
                    <td class="p-3 px-5"><%= task.getAssignedTo().getUsername()!= null ? task.getAssignedTo().getUsername() : "N/A" %></td>
                    <td class="p-3 px-5 flex space-x-2">
                        <a href="tasks?action=edit&id=<%= task.getId() %>" class="px-4 py-2 bg-green-500 hover:bg-green-600 text-white rounded-lg transition duration-200">Edit</a>
                        <a href="tasks?action=delete&id=<%= task.getId() %>" class="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg transition duration-200">Delete</a>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="7" class="p-3 text-center">No tasks found.</td>
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
        <p>&copy; 2024 Task Management System. All rights reserved.</p>
    </div>
</footer>

</body>
</html>
