<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
      <!-- Use RouterLink for the logo to navigate to the home page -->
      <RouterLink class="navbar-brand" to="/">Office Olympics</RouterLink>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <RouterLink class="nav-link" to="/login">Login</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/register">Sign Up</RouterLink>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup></script>
