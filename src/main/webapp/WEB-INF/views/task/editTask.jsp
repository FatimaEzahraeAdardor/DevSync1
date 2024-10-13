<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.devsync1.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.devsync1.entities.Tag" %>
<%@ page import="org.example.devsync1.entities.Task" %>
<html lang="en">
<head>
    <title>Edit Task</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.1.0-rc.0/css/select2.min.css" rel="stylesheet" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.1.0-rc.0/js/select2.min.js"></script>
</head>
<body class="bg-gray-100 text-gray-900 min-h-screen flex flex-col">

<!-- Navbar -->
<nav class="bg-gradient-to-r from-blue-600 to-indigo-600 p-4 shadow-lg">
    <div class="container mx-auto flex justify-between items-center">
        <h1 class="text-white text-3xl font-bold">Dashboard</h1>
    </div>
</nav>

<!-- Main layout -->
<div class="flex flex-grow">
    <!-- Include the sidebar -->
    <jsp:include page="../layout/side_bar.jsp" />

    <!-- Main content -->
    <div class="container mx-auto mt-8 flex-grow p-8 bg-white rounded-lg shadow-lg">
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-4xl font-extrabold text-gray-800">Edit Task</h1>
        </div>

        <!-- Error message display -->
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="mb-4 p-4 text-red-700 bg-red-200 rounded-lg shadow-md">
            <strong>Error:</strong> <%= error %>
        </div>
        <%
            }
        %>

        <!-- Form to edit a task -->
        <form action="tasks" method="post" class="space-y-6">
            <input type="hidden" name="action" value="update"/>
            <input type="hidden" name="id" value="<%= ((Task) request.getAttribute("task")).getId() %>"/>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- Task Title -->
                <div>
                    <label for="title" class="block text-sm font-semibold text-gray-700">Task Title</label>
                    <input type="text" name="title" id="title" value="<%= ((Task) request.getAttribute("task")).getTitle() != null ? ((Task) request.getAttribute("task")).getTitle() : "" %>" class="mt-2 block w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:ring-blue-500 focus:border-blue-500" required>
                </div>

                <!-- Task Start Date -->
                <div>
                    <label for="createdate" class="block text-sm font-semibold text-gray-700">Start Date</label>
                    <input type="date" name="createdate" id="createdate" value="<%= ((Task) request.getAttribute("task")).getCreationDate() != null ? ((Task) request.getAttribute("task")).getCreationDate().toString() : "" %>" class="mt-2 block w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:ring-blue-500 focus:border-blue-500" required>
                </div>

                <!-- Task Status -->
                <div>
                    <label for="status" class="block text-sm font-semibold text-gray-700">Status</label>
                    <select name="status" id="status" class="mt-2 block w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:ring-blue-500 focus:border-blue-500" required>
                        <option value="Pending" <%= ((Task) request.getAttribute("task")).getStatus().equals("Pending") ? "selected" : "" %>>Pending</option>
                        <option value="In Progress" <%= ((Task) request.getAttribute("task")).getStatus().equals("In Progress") ? "selected" : "" %>>In Progress</option>
                        <option value="Completed" <%= ((Task) request.getAttribute("task")).getStatus().equals("Completed") ? "selected" : "" %>>Completed</option>
                    </select>
                </div>

                <!-- Task Due Date -->
                <div>
                    <label for="dueDate" class="block text-sm font-semibold text-gray-700">Due Date</label>
                    <input type="date" name="dueDate" id="dueDate" value="<%= ((Task) request.getAttribute("task")).getDueDate() != null ? ((Task) request.getAttribute("task")).getDueDate().toString() : "" %>" class="mt-2 block w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:ring-blue-500 focus:border-blue-500" required>
                </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <%
                    User loggedUser = (User) session.getAttribute("loggedUser");
                %>
                <input type="hidden" name="createdBy" value="<%= loggedUser.getId() %>">

                <!-- Assigned To -->
                <div>
                    <label for="assignedTo" class="block text-sm font-semibold text-gray-700">Assigned To</label>
                    <select name="assignedTo" id="assignedTo" class="mt-2 block w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:ring-blue-500 focus:border-blue-500" required>
                        <%
                            List<User> users = (List<User>) request.getAttribute("users");
                            Task task = (Task) request.getAttribute("task");
                            if (users != null) {
                                for (User user : users) {
                        %>
                        <option value="<%= user.getId() %>" <%= user.getId().equals(task.getAssignedTo().getId()) ? "selected" : "" %>><%= user.getUsername() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <!-- Tags -->
                <div>
                    <label for="tag" class="block text-sm font-semibold text-gray-700">Tags</label>
                    <select id="tag" class="mt-2 block w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:ring-blue-500 focus:border-blue-500" name="tags[]" multiple="multiple" required>
                        <%
                            List<Tag> tags = (List<Tag>) request.getAttribute("tags");
                            List<Tag> selectedTags = task.getTags();
                            if (tags != null) {
                                for (Tag tag : tags) {
                        %>
                        <option value="<%= tag.getId() %>" <%= selectedTags.contains(tag) ? "selected" : "" %>><%= tag.getName() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- Task Description -->
                <div>
                    <label for="description" class="block text-sm font-semibold text-gray-700">Description</label>
                    <textarea name="description" id="description" rows="3" class="mt-2 block w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:ring-blue-500 focus:border-blue-500" required><%= task.getDescription() != null ? task.getDescription() : "" %></textarea>
                </div>
            </div>

            <!-- Submit button -->
            <div class="flex justify-end">
                <button type="submit" class="px-6 py-3 bg-gradient-to-r from-blue-700 to-indigo-700 hover:from-blue-800 hover:to-indigo-800 text-white font-medium rounded-lg shadow-md transition duration-300 ease-in-out">Update Task</button>
            </div>
        </form>
    </div>
</div>

<!-- Footer -->
<footer class="bg-gray-800 p-4 mt-auto">
    <div class="container mx-auto text-center text-white">
        <p>&copy; 2024 User Management System. All rights reserved.</p>
    </div>
</footer>

<script>
    $(document).ready(function(){
        $('#tag').select2();
    });
</script>

</body>
</html>
