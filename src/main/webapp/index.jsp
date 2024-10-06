<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to User Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet">
    <style>
        /* Custom styles for a cleaner background */
        body {
            background-color: #f9fafb; /* Light gray background for contrast */
        }
    </style>
</head>
<body class="text-gray-900 min-h-screen flex flex-col">

<!-- Navbar -->
<nav class="bg-blue-600 p-4 shadow-lg">
    <div class="container mx-auto flex justify-between items-center">
        <h1 class="text-white text-3xl font-bold">User Management System</h1>
       
    </div>
</nav>

<!-- Main content -->
<div class="container mx-auto mt-10 flex-grow p-8 bg-white rounded-lg shadow-lg">
    <div class="text-center">
        <h2 class="text-5xl font-extrabold mb-4">Welcome to the User Management System</h2>
        <p class="text-lg text-gray-700 mb-8">This system allows you to easily manage your users by adding, updating, or deleting user profiles. You can track their roles and maintain your user base effectively.</p>
    </div>

    <!-- Register and Login Section -->
    <div class="mt-12 text-center">
        <h3 class="text-4xl font-bold mb-4">Get Started</h3>
        <p class="text-lg text-gray-600 mb-6">Please register or log in to manage your users.</p>

        <div class="flex justify-center space-x-6">
            <a href="users?action=login" class="flex items-center justify-center text-white bg-gray-700 hover:bg-gray-800 transition duration-300 ease-in-out px-6 py-3 rounded-lg font-medium shadow-md hover:shadow-lg">
                <i class="fas fa-sign-in-alt mr-2"></i> Login
            </a>
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
