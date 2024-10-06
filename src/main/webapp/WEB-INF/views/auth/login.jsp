<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">

<!-- Login Form -->
<div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
  <h1 class="text-3xl font-bold text-center mb-6">Login</h1>

  <!-- Check for error message -->
  <% if (request.getAttribute("error") != null) { %>
  <div class="mb-4 text-red-600">
    <%= request.getAttribute("error") %>
  </div>
  <% } %>

  <form action="users?action=login" method="post">
    <div class="mb-4">
      <label for="email" class="block text-gray-700 font-semibold mb-2">Email</label>
      <input type="email" id="email" name="email" required
             class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600"
             placeholder="you@example.com">
    </div>

    <div class="mb-6">
      <label for="password" class="block text-gray-700 font-semibold mb-2">Password</label>
      <input type="password" id="password" name="password" required
             class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600">
    </div>

    <!-- Login Button -->
    <div>
      <button type="submit"
              class="w-full bg-blue-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-blue-700 focus:ring-4 focus:ring-blue-300 transition duration-300 ease-in-out">
        Login
      </button>
    </div>
  </form>

  <!-- Optional Links -->
  <div class="mt-6 text-center">
    <p class="text-gray-600">Don't have an account?
      <a href="register" class="text-blue-600 hover:underline">Register</a>
    </p>
  </div>
</div>

</body>
</html>
