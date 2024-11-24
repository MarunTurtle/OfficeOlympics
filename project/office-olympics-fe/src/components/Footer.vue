/**
 * @파일명: Footer.vue
 * @작성일: 2024-03-21
 * @설명: 모든 페이지 하단에 표시되는 공통 푸터 컴포넌트
 * @관련백엔드: 없음 (순수 프론트엔드 컴포넌트)
 */

<template>
  <!--
    푸터 영역
    copyright 정보 표시
  -->
  <footer class="footer">
    <p>&copy; 2024 Office Olympics. All rights reserved.</p>
  </footer>
</template>

<script setup></script>

<style scoped>
/*
  푸터 스타일링:
  - flex-shrink: 0으로 푸터 크기 고정
  - margin-top: auto로 항상 페이지 하단에 위치
  - secondary-color 배경색 사용
*/
.footer {
  flex-shrink: 0;
  background-color: var(--secondary-color);
  padding: 20px 0;
  margin-top: auto;
  text-align: center;
}
</style>
