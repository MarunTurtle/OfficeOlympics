import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth'; // Import the auth store
import Home from '@/pages/Home.vue';
import Login from '@/pages/Login.vue';
import Register from '@/pages/Register.vue';
import OlympicCreate from '@/pages/OlympicCreate.vue';
import ErrorPage from '@/pages/Error.vue';
// import ChallengeDetail from '@/pages/ChallengeDetail.vue';
// import ChallengeScore from '@/pages/ChallengeScore.vue';

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/auth/login', name: 'Login', component: Login },
  { path: '/auth/register', name: 'Register', component: Register },
  {
    path: '/olympic/create',
    name: 'OlympicCreate',
    component: OlympicCreate,
    meta: { requiresAuth: true }, // Require user to be logged in
  },
  {
    path: '/challenges/:id',
    name: 'ChallengeDetail',
    component: () => import('@/pages/ChallengeDetail.vue'), // Lazy load component
    props: true,
    meta: { requiresAuth: true }, // Require user to be logged in
  },
  {
    path: '/challenges/:id/score',
    name: 'ChallengeScore',
    component: () => import('@/pages/ChallengeScore.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/accounts/:userId',
    name: 'MyPage',
    component: () => import('@/pages/MyPage.vue'),
    meta: { requiresAuth: true },
    props: true
  },
  { path: '/:pathMatch(.*)*', name: 'Error', component: ErrorPage },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Add navigation guards
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore(); // Access the auth store

  // Ensure the user's session is loaded from localStorage
  if (!authStore.user) {
    authStore.loadUser(); // Load user from localStorage if not already loaded
  }

  // Handle protected routes
  if (to.meta.requiresAuth && !authStore.user) {
    return next({ name: 'Login', query: { redirect: to.fullPath } }); // Redirect to login with query
  }

  // Redirect logged-in users away from Login/Register
  if ((to.name === 'Login' || to.name === 'Register') && authStore.user) {
    return next({ name: 'Home' });
  }

  // Allow navigation to the requested route
  next();
});

export default router;
