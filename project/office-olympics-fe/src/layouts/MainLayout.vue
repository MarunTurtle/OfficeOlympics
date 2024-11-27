/**
 * @파일명: MainLayout.vue
 * @설명: 메인 콘텐츠를 위한 기본 레이아웃 컴포넌트
 */

<template>
  <!--
    메인 컨텐츠 컨테이너
    - Bootstrap의 container 클래스 사용
    - 반응형 최대 너비와 자동 마진 적용
  -->
  <div class="container">
    <!-- slot을 통해 페이지별 실제 컨텐츠가 주입됨 -->
    <slot />
  </div>
</template>

<script setup></script>
