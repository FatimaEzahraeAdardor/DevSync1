<%@ page import="org.example.devsync1.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body>
<div class="text-gray-900 bg-gray-200">
    <div class="p-4 flex">
        <h1 class="text-3xl">Users</h1>
    </div>
    <div class="flex items-center mt-4 gap-x-3">
        <a href="users?action=create" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5">Add User</a>
    </div>
    <div class="px-3 py-4 flex justify-center">
        <table class="w-full text-md bg-white shadow-md rounded mb-4">
            <thead>
            <tr class="border-b">
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
                List<User> users = (List<User>) request.getAttribute("users");
                if (users != null && !users.isEmpty()) {
                    for (User user : users) {
            %>
            <tr class="border-b hover:bg-orange-100">
                <td class="p-3 px-5"><%= user.getId() != null ? user.getId() : "N/A" %></td>
                <td class="p-3 px-5"><%= user.getUsername() != null ? user.getUsername() : "N/A" %></td>
                <td class="p-3 px-5"><%= user.getFirstName() != null ? user.getFirstName() : "N/A" %></td>
                <td class="p-3 px-5"><%= user.getLastName() != null ? user.getLastName() : "N/A" %></td>
                <td class="p-3 px-5"><%= user.getEmail() != null ? user.getEmail() : "N/A" %></td>
                <td class="p-3 px-5"><%= user.getRole() != null ? user.getRole() : "N/A" %></td>
                <td class="p-3 px-5 flex ">
                    <a href="users?action=edit&id=<%= user.getId() %>" class="px-4 py-2 bg-green-500 border rounded-lg text-sm font-medium text-gray-600 transition-colors duration-200 hover:bg-gray-100">edit</a>
                    <a href="users?action=delete&id=<%= user.getId() %>" class="px-4 py-2 bg-red-500 border rounded-lg text-sm font-medium text-gray-600 transition-colors duration-200 hover:bg-gray-100">delete</a>
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
</body>
</html>
