/**
 * @파일명: RecommendedChallengeCard.vue
 * @설명: 추천 챌린지를 표시하는 카드 컴포넌트
 * @관련백엔드: GET /api/challenges/{challengeId} 응답의 개별 챌린지 데이터를 표시
 */

<template>
  <!--
    추천 챌린지 카드 컨테이너
    카드 전체가 클릭 가능한 영역
  -->
  <div class="recommended-challenge-card">
    <!--
      썸네일 영역
      YouTube 영상의 썸네일을 16:9 비율로 표시
    -->
    <div class="thumbnail-wrapper">
      <img :src="thumbnailUrl" :alt="title" class="card-img">
    </div>

    <!--
      카드 본문
      제목과 숨겨진 링크 포함
    -->
    <div class="card-body">
      <h4 class="card-title">{{ title }}</h4>
      <!-- stretched-link로 카드 전체 영역을 클릭 가능하게 만듦 -->
      <RouterLink :to="`/challenges/${id}`" class="stretched-link">
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
/**
 * @컴포넌트명: RecommendedChallengeCard
 * @props: {
 *   id: 챌린지 고유 식별자 (백엔드 DB PK)
 *   title: 챌린지 제목
 *   videoUrl: YouTube 영상 URL
 * }
 */

import { computed } from 'vue';

// Props 정의 및 유효성 검사
const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  videoUrl: {
    type: String,
    required: true
  }
});

/**
 * YouTube 썸네일 URL 생성
 * videoUrl에서 video ID를 추출하여 YouTube 썸네일 API URL 생성
 * maxresdefault.jpg: 최고 품질의 썸네일 이미지 (1920x1080)
 */
const thumbnailUrl = computed(() => {
  const videoId = props.videoUrl.split('/').pop();
  return `https://img.youtube.com/vi/${videoId}/maxresdefault.jpg`;
});
</script>

<style scoped>
/*
  카드 스타일링:
  - 부드러운 그림자 효과
  - 호버 시 위로 떠오르는 애니메이션
  - 둥근 모서리 처리
*/
.recommended-challenge-card {
  position: relative;
  margin-bottom: 1rem;
  border-radius: 8px;
  overflow: hidden;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

/*
  썸네일 컨테이너
  16:9 비율 유지를 위한 패딩 처리
*/
.thumbnail-wrapper {
  position: relative;
  width: 100%;
  padding-top: 56.25%; /* 16:9 Aspect Ratio */
  overflow: hidden;
}

/* 썸네일 이미지 크기 및 위치 조정 */
.card-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 카드 본문 여백 설정 */
.card-body {
  padding: 0.75rem 1rem;
}

/*
  제목 스타일링
  - 2줄 제한
  - 말줄임표 처리
*/
.card-title {
  margin: 0;
  font-size: 1rem;
  line-height: 1.4;
  height: 2.0em;
  font-weight: 500;
  color: #2c3e50;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/*
  전체 영역 클릭 가능하도록 처리
  가상 요소로 전체 영역 커버
*/
.stretched-link::after {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 1;
  content: "";
}

/* 호버 효과 */
.recommended-challenge-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}
</style>
