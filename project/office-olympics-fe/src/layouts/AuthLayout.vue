/**
 * @파일명: AuthLayout.vue
 * @설명: 인증 관련 페이지(로그인/회원가입)의 공통 레이아웃 컴포넌트
 */

<template>
  <!--
    인증 페이지 레이아웃 컨테이너
    전체 화면 높이를 사용하며 중앙 정렬
  -->
  <div class="auth-layout">
    <!--
      실제 컨텐츠를 담는 컨테이너
      적절한 최대 너비와 여백을 가짐
    -->
    <div class="auth-container">
      <!-- slot을 통해 실제 인증 폼이 주입됨 -->
      <slot />
    </div>
  </div>
</template>

<script setup></script>

<style scoped></style>
