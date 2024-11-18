import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/pages/Home.vue';
import Login from '@/pages/Login.vue';
import Register from '@/pages/Register.vue';
import OlympicCreate from '@/pages/OlympicCreate.vue';
import ErrorPage from '@/pages/Error.vue';
import ChallengeDetail from '@/pages/ChallengeDetail.vue';

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/olympic/create', name: 'OlympicCreate', component: OlympicCreate },
  { path: '/challenges/:id', name: 'ChallengeDetail', component: () => import('@/pages/ChallengeDetail.vue'), // Lazy load component
    props: true, },
  { path: '/:pathMatch(.*)*', name: 'Error', component: ErrorPage },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
