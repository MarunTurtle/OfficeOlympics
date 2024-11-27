/**
 * @파일명: main.js
 * @설명: Vue 애플리케이션의 진입점
 * @구성요소:
 *   - Vue 앱 인스턴스 생성
 *   - Pinia 상태 관리 설정
 *   - 라우터 설정
 *   - 전역 스타일 적용
 */

// 전역 스타일시트 임포트
import './assets/styles/main.css';

// 핵심 의존성 임포트
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import { useAuthStore } from '@/stores/auth';

// Vue 앱 인스턴스 및 Pinia 스토어 생성
const app = createApp(App);
const pinia = createPinia();

// 플러그인 등록
app.use(pinia);
app.use(router);

/**
 * 앱 시작 시 사용자 인증 상태 복구
 * - 로컬 스토리지에서 저장된 사용자 정보 로드
 */
const authStore = useAuthStore();
authStore.loadUser();

// DOM에 앱 마운트
app.mount('#app');
