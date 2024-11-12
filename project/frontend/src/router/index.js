// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import RegisterView from '@/views/RegisterView.vue';
import OlympicCreate from '@/views/OlympicCreate.vue';
import ChallengeListView from '@/views/ChallengeListView.vue';
import ChallengeDetailView from '@/views/ChallengeDetailView.vue';
import MyPageView from '@/views/MyPageView.vue';

const routes = [
  { path: '/', component: HomeView },
  { path: '/login', component: LoginView },
  { path: '/register', component: RegisterView },
  { path: '/olympic/create', component: OlympicCreate },
  { path: '/challenges', component: ChallengeListView },
  { path: '/challenges/:id', component: ChallengeDetailView, props: true },
  { path: '/mypage', component: MyPageView },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
