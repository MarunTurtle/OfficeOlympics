/**
 * @파일명: Error.vue
 * @설명: 404 및 기타 에러 상황을 처리하는 에러 페이지 컴포넌트
 * @관련백엔드: 없음 (순수 프론트엔드 컴포넌트)
 */

<template>
  <MainLayout>
    <!--
      에러 페이지 컨테이너
      중앙 정렬된 에러 메시지와 홈 버튼 표시
    -->
    <div class="error-page">
      <!-- 에러 제목 -->
      <h1 class="text-danger text-center">앗! 문제가 발생했습니다.</h1>

      <!-- 에러 설명 -->
      <p class="text-center">
        찾으시는 페이지가 삭제되었거나, 일시적으로 사용할 수 없습니다.
      </p>

      <!-- 홈으로 이동하는 네비게이션 버튼 -->
      <div class="text-center mt-4">
        <RouterLink to="/" class="btn btn-primary">홈으로 이동</RouterLink>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import MainLayout from '@/layouts/MainLayout.vue';
</script>

<style scoped>
/*
  에러 페이지 스타일링:
  - 중앙 정렬
  - 적절한 여백
  - 반응형 폰트 크기
*/
.error-page {
  max-width: 600px;
  margin: 50px auto;
  padding: 20px;
  text-align: center;
}

/* 에러 제목 스타일 */
.error-page h1 {
  font-size: 2.5rem;
  margin-bottom: 20px;
}

/* 에러 설명 텍스트 스타일 */
.error-page p {
  font-size: 1.2rem;
  color: #6c757d;
}
</style>
