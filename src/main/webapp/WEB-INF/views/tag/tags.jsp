<%@ page import="org.example.devsync1.entities.Tag" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Tag Management</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        @media (max-width: 640px) {
            .table-responsive {
                overflow-x: auto;
            }
        }
    </style>
    <script>
        function openModal(modalId) {
            document.getElementById(modalId).classList.remove('hidden');
        }

        function closeModal(modalId) {
            document.getElementById(modalId).classList.add('hidden');
        }

        function setEditModalValues(id, name) {
            document.getElementById('editTagId').value = id;
            document.getElementById('editTagName').value = name;
        }
    </script>
</head>
<body class="bg-gray-100 text-gray-900 min-h-screen flex flex-col">

<!-- Navbar -->
<nav class="bg-blue-600 p-4 shadow-lg">
    <div class="container mx-auto flex justify-between items-center">
        <h1 class="text-white text-3xl font-bold">Tag Management</h1>
    </div>
</nav>

<!-- Main layout -->
<div class="flex flex-grow">
    <!-- Include the sidebar (same sidebar as Task Management page) -->
    <jsp:include page="../layout/side_bar.jsp" />

    <!-- Main content -->
    <div class="container mx-auto mt-8 flex-grow p-6 bg-white rounded-lg shadow-lg">
        <%
            // Retrieve tags from request attribute
            List<Tag> tags = (List<Tag>) request.getAttribute("tags");
        %>

        <div class="flex justify-between items-center mb-4">
            <h1 class="text-3xl font-bold">Manage Tags</h1>
            <!-- Button to open the create modal -->
            <button onclick="openModal('createTagModal')" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5">Add Tag</button>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <!-- Example Card for Users -->
            <div class="bg-white p-4 rounded-lg shadow">
                <h2 class="font-bold text-xl mb-2">Total Users</h2>
                <p class="text-3xl"><%= tags != null ? tags.stream().count() : 0 %></p>
            </div>
        </div>


        <!-- Table to display tags -->
        <div class="table-responsive">
            <table class="min-w-full bg-white shadow-md rounded-lg overflow-hidden">
                <thead>
                <tr class="border-b bg-gray-200">
                    <th class="text-left p-3 px-5">ID</th>
                    <th class="text-left p-3 px-5">Name</th>
                    <th class="text-left p-3 px-5">Actions</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (tags != null && !tags.isEmpty()) {
                        for (Tag tag : tags) {
                %>
                <tr class="border-b hover:bg-orange-100 transition-colors duration-200">
                    <td class="p-3 px-5"><%= tag.getId()!= null ? tag.getId() : "N/A" %></td>
                    <td class="p-3 px-5"><%= tag.getName()!= null ? tag.getName() : "N/A" %></td>
                    <td class="p-3 px-5 flex space-x-2">
                        <!-- Edit Button triggers the edit modal and fills in values -->
                        <button onclick="openModal('editTagModal'); setEditModalValues('<%= tag.getId() %>', '<%= tag.getName() %>')" class="px-4 py-2 bg-green-500 hover:bg-green-600 text-white rounded-lg transition duration-200">Edit</button>
                        <a href="tags?action=delete&id=<%= tag.getId() %>" class="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg transition duration-200">Delete</a>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="3" class="p-3 text-center">No tags found.</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Create Tag Modal -->
<div id="createTagModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 hidden flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-lg">
        <h2 class="text-2xl font-bold mb-4">Create New Tag</h2>
        <form action="tags" method="post">
            <input type="hidden" name="action" value="create">
            <div class="mb-4">
                <label for="tagName" class="block text-sm font-medium text-gray-700">Tag Name</label>
                <input type="text" id="tagName" name="name" required class="mt-1 block w-full p-2 border border-gray-300 rounded-md">
            </div>
            <div class="flex justify-end">
                <button type="button" onclick="closeModal('createTagModal')" class="text-gray-700 bg-gray-300 hover:bg-gray-400 font-medium rounded-lg text-sm px-5 py-2.5 mr-2">Cancel</button>
                <button type="submit" class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-sm px-5 py-2.5">Create</button>
            </div>
        </form>
    </div>
</div>

<!-- Edit Tag Modal -->
<div id="editTagModal" class="fixed inset-0 bg-gray-900 bg-opacity-50 hidden flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-lg">
        <h2 class="text-2xl font-bold mb-4">Edit Tag</h2>
        <form action="tags" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" id="editTagId" name="id">
            <div class="mb-4">
                <label for="editTagName" class="block text-sm font-medium text-gray-700">Tag Name</label>
                <input type="text" id="editTagName" name="name" required class="mt-1 block w-full p-2 border border-gray-300 rounded-md">
            </div>
            <div class="flex justify-end">
                <button type="button" onclick="closeModal('editTagModal')" class="text-gray-700 bg-gray-300 hover:bg-gray-400 font-medium rounded-lg text-sm px-5 py-2.5 mr-2">Cancel</button>
                <button type="submit" class="text-white bg-blue-700 hover:bg-blue-800 font-medium rounded-lg text-sm px-5 py-2.5">Update</button>
            </div>
        </form>
    </div>
</div>

<!-- Footer -->
<footer class="bg-gray-800 p-4 mt-auto">
    <div class="container mx-auto text-center text-white">
        <p>&copy; 2024 Tag Management System. All rights reserved.</p>
    </div>
</footer>

</body>
</html>
