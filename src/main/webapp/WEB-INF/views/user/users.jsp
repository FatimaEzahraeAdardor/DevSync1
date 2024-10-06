<%@ page import="org.example.devsync1.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>User Management Dashboard</title>
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
    <!-- Include the sidebar -->
    <jsp:include page="../layout/side_bar.jsp" />

    <!-- Main content -->
    <div class="container mx-auto mt-8 flex-grow p-6 bg-white rounded-lg shadow-lg">
        <%
            // Retrieve users from request attribute
            List<User> users = (List<User>) request.getAttribute("users");
        %>

        <div class="flex justify-between items-center mb-4">
            <h1 class="text-3xl font-bold">User Management</h1>
            <a href="users?action=create" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5">Add User</a>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <!-- Example Card for Users -->
            <div class="bg-white p-4 rounded-lg shadow">
                <h2 class="font-bold text-xl mb-2">Total Users</h2>
                <p class="text-3xl"><%= users != null ? users.size() : 0 %></p>
            </div>
        </div>

        <div class="table-responsive">
            <table class="min-w-full bg-white shadow-md rounded-lg overflow-hidden">
                <thead>
                <tr class="border-b bg-gray-200">
                    <th class="text-left p-3 px-5">ID</th>
                    <th class="text-left p-3 px-5">User Name</th>
                    <th class="text-left p-3 px-5">First Name</th>
                    <th class="text-left p-3 px-5">Last Name</th>
                    <th class="text-left p-3 px-5">Email</th>
                    <th class="text-left p-3 px-5">Role</th>
                    <th class="text-left p-3 px-5">Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (users != null && !users.isEmpty()) {
                        for (User user : users) {
                %>
                <tr class="border-b hover:bg-orange-100 transition-colors duration-200">
                    <td class="p-3 px-5"><%= user.getId() != null ? user.getId() : "N/A" %></td>
                    <td class="p-3 px-5"><%= user.getUsername() != null ? user.getUsername() : "N/A" %></td>
                    <td class="p-3 px-5"><%= user.getFirstName() != null ? user.getFirstName() : "N/A" %></td>
                    <td class="p-3 px-5"><%= user.getLastName() != null ? user.getLastName() : "N/A" %></td>
                    <td class="p-3 px-5"><%= user.getEmail() != null ? user.getEmail() : "N/A" %></td>
                    <td class="p-3 px-5"><%= user.getRole() != null ? user.getRole() : "N/A" %></td>
                    <td class="p-3 px-5 flex space-x-2">
                        <a href="users?action=edit&id=<%= user.getId() %>" class="px-4 py-2 bg-green-500 hover:bg-green-600 text-white rounded-lg transition duration-200">Edit</a>
                        <a href="users?action=delete&id=<%= user.getId() %>" class="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg transition duration-200">Delete</a>
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
