<%@ page import="org.example.devsync1.entities.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Edit Account</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 text-gray-900 min-h-screen flex flex-col">
<!-- Navbar -->
<nav class="bg-blue-600 p-4 shadow-lg">
    <div class="container mx-auto flex justify-between items-center">
        <h1 class="text-white text-3xl font-bold">User Management System</h1>
    </div>
</nav>

<!-- Main Content -->
<div class="min-h-screen py-6 flex flex-col justify-center sm:py-12">
    <div class="relative py-3 sm:max-w-3xl sm:mx-auto">
        <div class="absolute inset-0 bg-gradient-to-r from-blue-300 to-blue-600 shadow-lg transform -skew-y-6 sm:skew-y-0 sm:-rotate-6 sm:rounded-3xl"></div>
        <div class="relative px-6 py-12 bg-white shadow-lg sm:rounded-3xl sm:p-20">
            <h1 class="text-3xl font-semibold text-center">Edit Account</h1>
            <form action="users" method="post">
                <input type="hidden" name="action" value="update"/>
                <input type="hidden" name="id" value="<%= ((User) request.getAttribute("user")).getId() %>"/>
                <div class="divide-y divide-gray-200">
                    <div class="py-10 text-base leading-7 space-y-6 text-gray-700 sm:text-lg sm:leading-8">
                        <div class="relative">
                            <input autocomplete="off" id="username" name="username" type="text" value="<%= ((User) request.getAttribute("user")).getUsername() %>"
                                   class="peer placeholder-transparent h-12 w-full border-b-2 border-gray-300 text-gray-900
                                       focus:outline-none focus:border-blue-600" placeholder="User name" required />
                            <label for="username" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">User Name</label>
                        </div>
                        <div class="relative">
                            <input autocomplete="off" id="firstName" name="firstName" type="text" value="<%= ((User) request.getAttribute("user")).getFirstName() %>"
                                   class="peer placeholder-transparent h-12 w-full border-b-2 border-gray-300 text-gray-900
                                       focus:outline-none focus:border-blue-600" placeholder="First name" required />
                            <label for="firstName" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">First Name</label>
                        </div>
                        <div class="relative">
                            <input autocomplete="off" id="lastName" name="lastName" type="text" value="<%= ((User) request.getAttribute("user")).getLastName() %>"
                                   class="peer placeholder-transparent h-12 w-full border-b-2 border-gray-300 text-gray-900
                                       focus:outline-none focus:border-blue-600" placeholder="Last name" required />
                            <label for="lastName" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">Last Name</label>
                        </div>
                        <div class="relative">
                            <input autocomplete="off" id="email" name="email" type="email" value="<%= ((User) request.getAttribute("user")).getEmail() %>"
                                   class="peer placeholder-transparent h-12 w-full border-b-2 border-gray-300 text-gray-900
                                       focus:outline-none focus:border-blue-600" placeholder="Email address" required />
                            <label for="email" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">Email Address</label>
                        </div>
                        <div class="relative">
                            <input autocomplete="off" id="password" name="password" type="password" value="<%= ((User) request.getAttribute("user")).getPassword() %>"
                                   class="peer placeholder-transparent h-12 w-full border-b-2 border-gray-300 text-gray-900
                                       focus:outline-none focus:border-blue-600" placeholder="Password" required />
                            <label for="password" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">Password</label>
                        </div>
                        <div class="relative">
                            <select id="role" name="role" class="peer placeholder-transparent h-12 w-full border-b-2
                                       border-gray-300 text-gray-900 focus:outline-none focus:border-blue-600" required>
                                <option value="USER" <%= ((User) request.getAttribute("user")).getRole().equals("USER") ? "selected" : "" %>>User</option>
                                <option value="MANAGER" <%= ((User) request.getAttribute("user")).getRole().equals("MANAGER") ? "selected" : "" %>>Manager</option>
                            </select>
                            <label for="role" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">Role</label>
                        </div>
                        <div class="relative">
                            <button type="submit" class="bg-blue-500 text-white rounded-md px-6 py-3 hover:bg-blue-600 transition duration-200">Update User</button> <!-- Increased padding -->
                        </div>
                    </div>
                </div>
            </form>
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
