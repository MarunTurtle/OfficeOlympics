<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
      <!-- Logo -->
      <RouterLink class="navbar-brand" to="/">
        <img src="@/assets/images/logo.png" alt="오피스 올림픽 로고" class="navbar-logo">
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
              <RouterLink class="nav-button btn" to="/auth/login">로그인</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-button btn" to="/auth/register">회원가입</RouterLink>
            </li>
          </template>

          <!-- Case 2: Logged In & Created Olympic -->
          <template v-else-if="isLoggedIn && hasOlympics">
            <li class="nav-item">
              <RouterLink class="nav-button btn" :to="`/accounts/${authStore.user.id}`">마이페이지</RouterLink>
            </li>
            <li class="nav-item">
              <button class="btn btn-tertiary" @click="onLogout">로그아웃</button>
            </li>
          </template>

          <!-- Case 3: Logged In & No Olympic ID -->
          <template v-else-if="isLoggedIn && !hasOlympics">
            <li class="nav-item">
              <RouterLink class="nav-button btn btn-warning" to="/olympic/create">올림픽 만들기</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-button btn" to="/accounts/{{ authStore.user.id }}">마이페이지</RouterLink>
            </li>
            <li class="nav-item">
              <button class="btn btn-tertiary" @click="onLogout">로그아웃</button>
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
    await authStore.logoutUser();
    alert("로그아웃되었습니다.");
    router.push('/');
  } catch (error) {
    console.error("Logout failed:", error);
    alert("로그아웃 중 오류가 발생했습니다.");
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

.btn-tertiary {
  font-size: 1rem;
  font-weight: 600;
  padding: 8px 16px;
  margin: 0 5px;
  color: #333;
  background-color: var(--tertiary-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.btn-tertiary:hover {
  background-color: var(--alert-color);
  transform: scale(1.05);
}

.btn-tertiary:active {
  transform: scale(0.95);
}
</style>
