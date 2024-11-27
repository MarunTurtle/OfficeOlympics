# 인증 및 사용자 관리

## 목차
- [인증 및 사용자 관리](#인증-및-사용자-관리)
  - [목차](#목차)
  - [1. 기본 구조 이해하기](#1-기본-구조-이해하기)
  - [2. 개요](#2-개요)
  - [3. 주요 기능](#3-주요-기능)
    - [3.1 로그인 기능](#31-로그인-기능)
    - [3.2 회원가입 기능](#32-회원가입-기능)
    - [3.3 인증 상태 관리 (Auth Store)](#33-인증-상태-관리-auth-store)
    - [3.4 마이페이지 기능](#34-마이페이지-기능)
  - [4. 라우터 보호](#4-라우터-보호)
  - [5. 사용자 경험 (UX) 개선](#5-사용자-경험-ux-개선)

## 1. 기본 구조 이해하기
1. **컴포넌트 이해하기**:
   - `Login.vue`와 `Register.vue`는 각각 독립적인 페이지 컴포넌트입니다.
   - `AuthLayout.vue`는 인증 관련 페이지의 공통 레이아웃을 제공합니다.

2. **상태 관리 이해하기**:
   - Pinia 스토어(`auth.js`)는 전역 상태 관리를 담당합니다.
   - `useAuthStore()`를 통해 어디서든 인증 상태에 접근할 수 있습니다.

3. **라우팅 이해하기**:
   - `router/index.js`에서 페이지 간 이동을 관리합니다.
   - 인증이 필요한 페이지는 `meta: { requiresAuth: true }`로 표시됩니다.

4. **데이터 흐름**:
```
사용자 입력 → 유효성 검사 → API 요청 → 상태 업데이트 → UI 업데이트
```

이러한 구조를 통해 안전하고 사용자 친화적인 인증 시스템을 구현하고 있습니다.

## 2. 개요
Office Olympics의 인증 시스템은 Vue 3와 Pinia를 활용하여 구현되었습니다. 이 시스템은 사용자 인증, 회원가입, 프로필 관리 등의 기능을 제공하여 사용자 경험을 향상시킵니다.

## 3. 주요 기능

### 3.1 로그인 기능
- 이메일과 비밀번호를 통한 로그인
- 이메일 형식 및 비밀번호 필수 입력에 대한 유효성 검사
- 로그인 성공 및 실패에 대한 피드백 제공
- 로그인 후 자동으로 리다이렉션

**사용된 컴포넌트**: `Login.vue`, `AuthLayout.vue`

참고 코드:

```8:62:office-olympics-fe/src/pages/Login.vue
<template>
  <AuthLayout>
    <!-- 로그인 폼 컨테이너 -->
    <div class="auth-form">
      <h1 class="text-center mb-4">로그인</h1>
      <!-- 로그인 폼 - 이메일과 비밀번호 입력 -->
      <form @submit.prevent="onLogin">
        <!-- 이메일 입력 필드 -->
        <div class="mb-3">
          <label for="email" class="form-label">이메일</label>
          <input
            type="email"
            id="email"
            class="form-control input-field"
            v-model="email"
            :class="{ 'is-invalid': emailError }"
            placeholder="이메일을 입력하세요"
            required
          />
          <!-- 이메일 유효성 검사 에러 메시지 -->
          <div v-if="emailError" class="invalid-feedback">
            유효한 이메일 주소를 입력해주세요.
          </div>
        </div>

        <!-- 비밀번호 입력 필드 -->
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호</label>
          <input
            type="password"
            id="password"
            class="form-control input-field"
            v-model="password"
            :class="{ 'is-invalid': passwordError }"
            placeholder="비밀번호를 입력하세요"
            required
          />
          <!-- 비밀번호 유효성 검사 에러 메시지 -->
          <div v-if="passwordError" class="invalid-feedback">
            비밀번호를 입력해주세요.
          </div>
        </div>

        <!-- 로그인 버튼 -->
        <button type="submit" class="btn btn-primary w-100">로그인</button>
      </form>

      <!-- 회원가입 링크 -->
      <p class="text-center mt-3">
        계정이 없으신가요?
        <RouterLink to="/auth/register" class="link-primary">회원가입</RouterLink>
      </p>
    </div>
  </AuthLayout>
</template>
```

### 3.2 회원가입 기능
- 사용자 정보 입력 (이름, 이메일, 닉네임, 비밀번호)
- 프로필 이미지 업로드
- 실시간 유효성 검사
- 회원가입 완료 후 로그인 페이지로 리다이렉션

**사용된 컴포넌트**: `Register.vue`, `AuthLayout.vue`

참고 코드:

```8:33:office-olympics-fe/src/pages/Register.vue
<template>
  <AuthLayout>
    <!-- 회원가입 폼 컨테이너 -->
    <div class="register-form">
      <h1 class="text-center mb-4">회원가입</h1>
      <form @submit.prevent="onRegister">
        <!-- 이름 입력 필드 -->
        <div class="mb-3">
          <label for="name" class="form-label">이름</label>
          <input type="text" id="name" class="form-control" v-model="name"
            placeholder="예시: 이케빈" @blur="nameTouched = true" />
          <!-- 이름 유효성 검사 에러 메시지 -->
          <small v-if="nameTouched && !isNotEmpty(name)" class="text-danger">
            이름을 입력해주세요.
          </small>
        </div>

        <!-- 이메일 입력 필드 -->
        <div class="mb-3">
          <label for="email" class="form-label">이메일</label>
          <input type="email" id="email" class="form-control" v-model="email" placeholder="예시: ssafy@example.com"
            @blur="emailTouched = true" />
          <small v-if="emailTouched && !isValidEmail(email)" class="text-danger">
            유효한 이메일을 입력해주세요.
          </small>
        </div>
```

### 3.3 인증 상태 관리 (Auth Store)
Pinia를 사용하여 전역적으로 인증 상태를 관리합니다:
- 로그인 상태 유지
- 토큰 관리
- 자동 로그인
- 로그아웃 처리

**사용된 컴포넌트**: `auth.js` (Pinia Store)

참고 코드:

```67:116:office-olympics-fe/src/stores/auth.js
    },

    /**
     * 회원가입 처리
     * @param {FormData} registerData - 회원가입 정보
     * @throws {Error} 회원가입 실패 시 에러
     */
    async registerUser(registerData) {
      try {
        const response = await register(registerData);
        console.log('Registration successful:', response.data);
        alert('회원가입이 완료되었습니다! 로그인해 주세요.');
      } catch (error) {
        console.error('Registration failed:', error);
        throw new Error('회원가입 중 오류가 발생했습니다.');
      }
    },

    /**
     * 로그아웃 처리
     * - 서버 로그아웃 요청
     * - 로컬 상태 초기화
     * - 로컬 스토리지 클리어
     */
    async logoutUser() {
      try {
        await logout();
        this.user = null;
        this.token = null;
        localStorage.removeItem('user');
        localStorage.removeItem('olympicsId');
      } catch (error) {
        console.error('Logout failed:', error);
      }
    },

    /**
     * 로컬 스토리지에서 사용자 정보 로드
     * - 페이지 새로고침 시 상태 복구에 사용
     */
    loadUser() {
      const user = localStorage.getItem('user');
      if (user) {
        this.user = JSON.parse(user);
      } else {
        this.user = null; // 로컬 스토리지에 사용자 정보가 없으면 초기화
      }
    },
  },
});
```

### 3.4 마이페이지 기능
- 사용자 프로필 정보 표시
- 프로필 수정
- 참여 중인 올림픽 정보 확인
- 계정 관리

**사용된 컴포넌트**: `MyPage.vue`, `MainLayout.vue`, `EditProfileModal.vue`

참고 코드:

```1:178:office-olympics-fe/src/pages/MyPage.vue
/**
 * @파일명: MyPage.vue
 * @설명: 사용자 프로필과 올림픽 정보를 표시하는 마이페이지 컴포넌트
 * @관련백엔드:
 *   - GET /api/users/{userId} (사용자 프로필 조회)
 *   - DELETE /api/users/{userId} (계정 삭제)
 */

<template>
  <MainLayout>
    <div class="my-page container py-5">
      <!-- 로딩 상태 표시 -->
      <div v-if="loading" class="loading-spinner">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">로딩중...</span>
        </div>
      </div>

      <!-- 에러 상태 표시 -->
      <div v-else-if="error" class="alert alert-danger shadow-sm">
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
        {{ error }}
      </div>

      <!-- 메인 컨텐츠 -->
      <div v-else class="row g-4">
        <!-- 사용자 프로필 섹션 -->
        <div class="col-md-4">
          <div class="card shadow-sm hover-shadow">
            <!-- 프로필 이미지와 기본 정보 -->
            <div class="card-body text-center p-4">
              <div class="mb-4">
                <img
                  :src="userData?.imgSrc || defaultProfileImage"
                  class="rounded-circle profile-image border border-3 border-white shadow"
                  alt="프로필 이미지"
                >
              </div>
              <h3 class="card-title fw-bold mb-2">{{ userData?.nickname }}</h3>
              <p class="text-muted mb-1">
                <i class="bi bi-envelope-fill me-2"></i>{{ userData?.email }}
              </p>
              <p class="text-muted">
                <i class="bi bi-person-fill me-2"></i>{{ userData?.name }}
              </p>
            </div>
          </div>
        </div>

        <!-- 올림픽 참가자 섹션 -->
        <div class="col-md-8">
          <div class="card shadow-sm hover-shadow">
            <div class="card-header bg-primary text-white py-3">
              <h4 class="mb-0 fw-bold">
                <i class="bi bi-trophy-fill me-2"></i>
                {{ players[0]?.olympics_name || '올림픽 정보' }}
              </h4>
            </div>
            <div class="card-body p-4">
              <div v-if="players.length === 0" class="text-center py-5">
                <i class="bi bi-people-fill text-muted fs-1 mb-3 d-block"></i>
                <p class="text-muted mb-0">생성된 올림픽이 없습니다</p>
              </div>
              <div v-else class="table-responsive">
                <table class="table table-hover align-middle">
                  <thead class="table-light">
                    <tr>
                      <th class="py-3">이름</th>
                      <th class="py-3 text-end">점수</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="player in players" :key="player.player_name">
                      <td class="py-3">
                        <i class="bi bi-person-circle me-2"></i>
                        {{ player.player_name }}
                      </td>
                      <td class="py-3 text-end fw-bold">
                        {{ player.total_score }}
                        <span class="ms-2">점</span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 작업 버튼 영역 -->
      <div class="row mt-5">
        <div class="col-12 text-center">
          <button class="btn btn-primary btn-lg me-3 px-4 shadow-sm" @click="editProfile">
            <i class="bi bi-pencil-fill me-2"></i>프로필 수정
          </button>
          <button class="btn btn-outline-danger btn-lg px-4" @click="confirmDelete">
            <i class="bi bi-trash-fill me-2"></i>계정 삭제
          </button>
        </div>
      </div>
    </div>
    <!-- 프로필 수정 모달 -->
    <EditProfileModal
      ref="editProfileModal"
      :userData="userData"
      :players="players"
      @update="fetchUserProfile"
    />
  </MainLayout>
</template>

<script setup>
/**
 * @컴포넌트명: MyPage
 * @설명: 사용자 프로필 관리 및 올림픽 정보 표시
 * @상태:
 *   - userData: 사용자 프로필 정보
 *   - players: 올림픽 참가자 목록
 *   - loading: 로딩 상태
 *   - error: 에러 메시지
 */

import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import { useUserStore } from '@/stores/user';
import { useAuthStore } from '@/stores/auth';
import defaultProfileImage from '@/assets/images/default_profile.png';
import EditProfileModal from '@/components/EditProfileModal.vue';

// 라우터 및 스토어 초기화
const router = useRouter();
const userStore = useUserStore();
const authStore = useAuthStore();

// 상태 관리
const userData = ref(null);
const players = ref([]);
const loading = ref(true);
const error = ref(null);
const editProfileModal = ref(null);

/**
 * 사용자 프로필 정보 조회
 * - 사용자 ID로 프로필 데이터 요청
 * - 프로필 이미지 처리
 * - 올림픽 참가자 정보 설정
 */
const fetchUserProfile = async () => {
  try {
    loading.value = true;
    error.value = null;

    console.log('Auth Store User:', authStore.user);
    const userId = authStore.user?.id || authStore.user?.userId;
    console.log('Extracted User ID:', userId);

    if (!userId) {
      throw new Error('No user ID found');
    }

    const response = await userStore.fetchUser(userId);
    console.log('Profile Response:', response);

    userData.value = {
      ...response.data.userData,
      imgSrc: response.data.userData.ImgSrc || response.data.userData.imgSrc || defaultProfileImage
    };
    players.value = response.data.players;

  } catch (err) {
    console.error('Error fetching profile:', err);
    error.value = err.message || 'Failed to load profile data';
  } finally {
    loading.value = false;
  }
};
```

## 4. 라우터 보호
인증이 필요한 페이지는 라우터 가드를 통해 보호됩니다:
- 비로그인 사용자의 접근 제한
- 인증된 사용자의 불필요한 로그인/회원가입 페이지 접근 방지

**사용된 컴포넌트**: `router/index.js`

참고 코드:

```92:131:office-olympics-fe/src/router/index.js
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
```

## 5. 사용자 경험 (UX) 개선
- 로딩 상태 표시
- 에러 메시지 피드백
- 직관적인 폼 유효성 검사
- 자동 리다이렉션