/**
 * @파일명: ChallengeCard.vue
 * @설명: 챌린지 카드 컴포넌트 - 챌린지 목록에서 각 챌린지를 표시하는 카드 형태의 컴포넌트
 * @관련백엔드: GET /api/challenges 응답의 개별 챌린지 데이터를 표시
 */

<template>
  <!-- 카드 전체 클릭 시 해당 챌린지 상세 페이지로 이동 -->
  <div class="challenge-card" @click="navigateToChallenge">
    <!-- 썸네일 영역: YouTube 썸네일 이미지 표시 -->
    <div class="thumbnail-wrapper">
      <img
        :src="thumbnailUrl"
        :alt="title"
        class="card-img-top"
        @error="handleImageError"
      >
    </div>

    <div class="card-body">
      <h5 class="card-title">{{ title }}</h5>
      <p class="card-description">{{ description }}</p>

      <!--
        도전하기 버튼
        @click.stop: 이벤트 버블링 방지 (카드 전체 클릭 이벤트와 충돌 방지)
      -->
      <RouterLink
        :to="challengeRoute"
        class="btn card-button mt-3"
        @click.stop
      >
        도전하기
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
/**
 * @컴포넌트명: ChallengeCard
 * @props: {
 *   id: 챌린지 고유 식별자 (백엔드 DB PK)
 *   title: 챌린지 제목
 *   description: 챌린지 설명
 *   videoUrl: YouTube 영상 URL
 * }
 */

import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { getYoutubeThumbnail } from '@/utils/youtube';

// Props 유효성 검사 포함 정의
const props = defineProps({
  id: {
    type: Number,
    required: true
  },
  title: {
    type: String,
    required: true,
    validator: value => value.length > 0 // 빈 문자열 방지
  },
  description: {
    type: String,
    required: true
  },
  videoUrl: {
    type: String,
    required: true
  }
});

const router = useRouter();
// 썸네일 로드 실패시 사용할 대체 이미지 경로
const fallbackImage = ref('/images/thumbnail-fallback.png');

// 컴퓨티드 속성
// YouTube URL에서 썸네일 URL 추출
const thumbnailUrl = computed(() => getYoutubeThumbnail(props.videoUrl));
// 챌린지 상세 페이지 라우트 경로 생성
const challengeRoute = computed(() => `/challenges/${props.id}`);

// 메서드
// 카드 클릭시 해당 챌린지 상세 페이지로 이동
const navigateToChallenge = () => {
  router.push(challengeRoute.value);
};

// 썸네일 이미지 로드 실패시 대체 이미지로 교체
const handleImageError = (e) => {
  e.target.src = fallbackImage.value;
};
</script>

<style scoped>
/* 챌린지 카드 컴포넌트의 스타일 정의 */

/* 카드 컨테이너 */
.challenge-card {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  cursor: pointer;
}

/* 카드 호버 효과: 위로 5px 이동 */
.challenge-card:hover {
  transform: translateY(-5px);
}

/* 썸네일 컨테이너: 16:9 비율 유지 */
.thumbnail-wrapper {
  position: relative;
  padding-top: 56.25%; /* 16:9 = (9/16 * 100)% */
  overflow: hidden;
}

/* 썸네일 이미지: 컨테이너에 맞춰 크기 조정 */
.card-img-top {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 카드 내용 영역 */
.card-body {
  padding: 1.25rem;
}

/* 카드 제목: 최대 2줄, 초과시 말줄임표 처리 */
.card-title {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 0.1rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 2.4em;
}

/* 카드 설명: 최대 3줄, 초과시 말줄임표 처리 */
.card-description {
  font-size: 1rem;
  color: #666;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 4.5em;
}

/* 도전하기 버튼: 기본 스타일 */
.card-button {
  width: 100%;
  font-weight: 600;
  color: white;
  background-color: var(--primary-color);
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s;
}

/* 도전하기 버튼: 호버 효과 */
.card-button:hover {
  background-color: var(--interaction-hover-color);
}
</style>
