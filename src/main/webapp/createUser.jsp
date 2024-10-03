<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Create New Account</title>
  <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body>
<h1>Hello!</h1>
<br/>
<div class="min-h-screen bg-gray-100 py-6 flex flex-col justify-center sm:py-12">
  <div class="relative py-3 sm:max-w-xl sm:mx-auto">
    <div class="absolute inset-0 bg-gradient-to-r from-blue-300 to-blue-600 shadow-lg transform -skew-y-6 sm:skew-y-0 sm:-rotate-6 sm:rounded-3xl"></div>
    <div class="relative px-4 py-10 bg-white shadow-lg sm:rounded-3xl sm:p-20">
      <h1 class="text-2xl font-semibold">Create New Account</h1>
      <form action="users" method="post">
        <input type="hidden" name="action" value="add"/>
        <div class="divide-y divide-gray-200">
          <div class="py-8 text-base leading-6 space-y-4 text-gray-700 sm:text-lg sm:leading-7">
            <div class="relative">
              <input autocomplete="off" id="username" name="username" type="text"
                     class="peer placeholder-transparent h-10 w-full border-b-2 border-gray-300 text-gray-900
                                              focus:outline-none focus:border-rose-600" placeholder="User name" required />
              <label for="username" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">User Name</label>
            </div>
            <div class="relative">
              <input autocomplete="off" id="firstName" name="firstName" type="text"
                     class="peer placeholder-transparent h-10 w-full border-b-2 border-gray-300 text-gray-900
                                              focus:outline-none focus:border-rose-600" placeholder="First name" required />
              <label for="firstName" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">First Name</label>
            </div>
            <div class="relative">
              <input autocomplete="off" id="lastName" name="lastName" type="text"
                     class="peer placeholder-transparent h-10 w-full border-b-2 border-gray-300 text-gray-900
                                              focus:outline-none focus:border-rose-600" placeholder="Last name" required />
              <label for="lastName" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">Last Name</label>
            </div>
            <div class="relative">
              <input autocomplete="off" id="email" name="email" type="email"
                     class="peer placeholder-transparent h-10 w-full border-b-2 border-gray-300 text-gray-900
                                              focus:outline-none focus:border-rose-600" placeholder="Email address" required />
              <label for="email" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">Email Address</label>
            </div>
            <div class="relative">
              <input autocomplete="off" id="password" name="password" type="password"
                     class="peer placeholder-transparent h-10 w-full border-b-2 border-gray-300 text-gray-900
                                              focus:outline-none focus:border-rose-600" placeholder="Password" required />
              <label for="password" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">Password</label>
            </div>
            <div class="relative">
              <select id="role" name="role" class="peer placeholder-transparent h-10 w-full border-b-2
                                       border-gray-300 text-gray-900 focus:outline-none focus:border-rose-600" required>
                <option value="USER">User</option>
                <option value="MANAGER">Manager</option>
              </select>
              <label for="role" class="absolute left-0 -top-3.5 text-gray-600 text-sm
                                       peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
                                       peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600
                                       peer-focus:text-sm">Role</label>
            </div>
            <div class="relative">
              <button type="submit" class="bg-blue-500 text-white rounded-md px-2 py-1">Add User</button>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
</body>
</html>
