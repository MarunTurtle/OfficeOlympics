/**
 * @파일명: router/index.js
 * @설명: Vue Router 설정 및 네비게이션 가드 정의
 */

import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import Home from '@/pages/Home.vue';
import Login from '@/pages/Login.vue';
import Register from '@/pages/Register.vue';
import OlympicCreate from '@/pages/OlympicCreate.vue';
import ErrorPage from '@/pages/Error.vue';

/**
 * 라우트 설정
 * @type {Array<RouteRecordRaw>}
 */
const routes = [
  /**
   * 공개 접근 가능 라우트
   * - 인증이 필요하지 않은 페이지들
   */
  { path: '/', name: 'Home', component: Home },
  { path: '/auth/login', name: 'Login', component: Login },
  { path: '/auth/register', name: 'Register', component: Register },

  /**
   * 인증이 필요한 라우트
   * - 로그인한 사용자만 접근 가능
   */
  {
    path: '/olympic/create',
    name: 'OlympicCreate',
    component: OlympicCreate,
    meta: { requiresAuth: true }, // 로그인 필요
  },

  /**
   * 동적 임포트를 사용하는 인증 필요 라우트
   * - 지연 로딩으로 초기 로드 시간 최적화
   */
  {
    path: '/challenges/:id',
    name: 'ChallengeDetail',
    component: () => import('@/pages/ChallengeDetail.vue'),
    props: true,
    meta: { requiresAuth: true },
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

  /**
   * 공개 접근 가능한 동적 임포트 라우트
   * - 인증 없이 접근 가능한 지연 로딩 페이지
   */
  {
    path: '/challenges/:id/rank',
    name: 'ChallengeRank',
    component: () => import('@/pages/ChallengeRank.vue')
  },
  {
    path: '/challenges/:id/final-rank',
    name: 'FinalRank',
    component: () => import('@/pages/FinalRank.vue')
  },

  // 404 에러 페이지
  { path: '/:pathMatch(.*)*', name: 'Error', component: ErrorPage },
];

/**
 * Vue Router 인스턴스 생성
 * - HTML5 History 모드 사용
 * - 정의된 라우트 설정 적용
 */
const router = createRouter({
  history: createWebHistory(),
  routes,
});

/**
 * 전역 네비게이션 가드
 * @param {RouteLocationNormalized} to - 이동할 라우트
 * @param {RouteLocationNormalized} from - 현재 라우트
 * @param {NavigationGuardNext} next - 네비게이션 컨트롤 함수
 *
 * 주요 기능:
 * 1. 사용자 세션 로드
 * 2. 보호된 라우트 접근 제어
 * 3. 인증된 사용자의 로그인/회원가입 페이지 접근 제어
 */
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  // localStorage에서 사용자 세션 로드
  if (!authStore.user) {
    authStore.loadUser();
  }

  /**
   * 보호된 라우트 처리
   * - 인증이 필요한 페이지에 대한 접근 제어
   */
  if (to.meta.requiresAuth && !authStore.user) {
    return next({
      name: 'Login',
      query: { redirect: to.fullPath } // 로그인 후 리다이렉션을 위한 쿼리 파라미터
    });
  }

  /**
   * 인증된 사용자의 로그인/회원가입 페이지 접근 제어
   */
  if ((to.name === 'Login' || to.name === 'Register') && authStore.user) {
    return next({ name: 'Home' });
  }

  // 요청된 라우트로 이동 허용
  next();
});

export default router;
