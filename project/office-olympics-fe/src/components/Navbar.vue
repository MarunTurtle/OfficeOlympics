<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
      <!-- Logo -->
      <RouterLink class="navbar-brand" to="/">
        <img src="@/assets/images/logo.png" alt="Office Olympics Logo" class="navbar-logo">
      </RouterLink>

      <!-- Toggle Button for Responsive Navbar -->
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <!-- Case 1: Logged Out -->
          <template v-if="!isLoggedIn">
            <li class="nav-item">
              <RouterLink class="nav-button btn" to="/auth/login">Login</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-button btn" to="/auth/register">Register</RouterLink>
            </li>
          </template>

          <!-- Case 2: Logged In & Created Olympic -->
          <template v-else-if="isLoggedIn && hasOlympics">
            <li class="nav-item">
              <RouterLink class="nav-button btn" to="/mypage">My Page</RouterLink>
            </li>
            <li class="nav-item">
              <button class="nav-button btn" @click="onLogout">Sign Out</button>
            </li>
          </template>

          <!-- Case 3: Logged In & No Olympic ID -->
          <template v-else-if="isLoggedIn && !hasOlympics">
            <li class="nav-item">
              <RouterLink class="nav-button btn btn-warning" to="/olympic/create">Create Olympics</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-button btn" to="/accounts/{{ authStore.user.id }}">My Page</RouterLink>
            </li>
            <li class="nav-item">
              <button class="nav-button btn" @click="onLogout">Sign Out</button>
            </li>
          </template>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useOlympicStore } from "@/stores/olympic";
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const olympicStore = useOlympicStore();
const router = useRouter();

// Reactive properties to determine state
const isLoggedIn = computed(() => !!authStore.user);
const hasOlympics = computed(() => !!olympicStore.userOlympicId);

// Logout handler
const onLogout = async () => {
  try {

    // If the user has an OlympicId, delete it from the backend
    // if (olympicStore.userOlympicId) {
    //   await olympicStore.deleteOlympicEvent(olympicStore.userOlympicId);
    // }    

    // Logout the user (clears user data on the frontend and backend)
    await authStore.logoutUser();

    alert("You have been logged out.");
    router.push('/'); // Redirect to the main page
  } catch (error) {
    console.error("Logout failed:", error);
    alert("An error occurred while logging out.");
  }
};
</script>

<style scoped>
.navbar {
  background-color: var(--secondary-color);
}

.navbar-logo {
  height: 3.75rem;
  width: auto;
  object-fit: contain;
}

.nav-link {
  margin-left: 10px;
}

/* Consistent Button Styles */
.nav-button {
  font-size: 1rem;
  font-weight: 600;
  padding: 8px 16px;
  margin: 0 5px;
  color: #fff;
  background-color: var(--primary-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.nav-button:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.05);
}

.nav-button.btn-warning {
  background-color: var(--warning-color);
}

.nav-button:active {
  transform: scale(0.95);
}
</style>
