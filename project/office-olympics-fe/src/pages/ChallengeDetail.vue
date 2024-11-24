/**
 * @파일명: ChallengeDetail.vue
 * @설명: 챌린지 상세 페이지 컴포넌트
 * @관련백엔드:
 *   - GET /api/challenges/{challengeId} (챌린지 상세 정보)
 */

<template>
  <MainLayout>
    <div class="challenge-detail container">
      <!-- 로딩 상태 표시 -->
      <div v-if="loading" class="text-center my-5 py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">로딩중...</span>
        </div>
      </div>

      <!-- 챌린지 상세 정보 표시 -->
      <div v-else-if="challenge" class="row g-4">
        <!-- 메인 콘텐츠 영역 (9칸) -->
        <div class="col-lg-9">
          <!-- 비디오 섹션: YouTube 임베드 -->
          <div class="content-section">
            <div class="video-container mb-4">
              <!-- 16:9 비율 유지를 위한 래퍼 -->
              <div class="video-wrapper">
                <iframe v-if="challenge.videoUrl"
                  :src="challenge.videoUrl"
                  frameborder="0"
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                  allowfullscreen>
                </iframe>
              </div>
            </div>

            <!-- 챌린지 정보 섹션 -->
            <div class="challenge-info p-4">
              <!-- 제목과 시작 버튼 -->
              <div class="title-action-container d-flex justify-content-between align-items-center mb-4">
                <h1 class="challenge-title mb-0">{{ challenge.title }}</h1>
                <button class="btn btn-primary" @click="startChallenge">
                  <i class="bi bi-play-circle me-2"></i>챌린지 시작하기
                </button>
              </div>

              <!-- 챌린지 설명 -->
              <div class="challenge-description">
                <h3 class="description-title mb-3">챌린지 설명</h3>
                <p class="description-text">{{ challenge.description }}</p>
              </div>
            </div>

            <!-- 댓글 섹션 -->
            <div class="comments-section mt-3">
              <Comments :challengeId="challengeId" />
            </div>
          </div>
        </div>

        <!-- 우측 사이드바 (3칸): 추천 챌린지 목록 -->
        <div class="col-lg-3">
          <div class="recommended-challenges">
            <h4 class="recommended-title mb-4">추천 챌린지</h4>
            <div class="recommended-list">
              <RecommendedChallengeCard
                v-for="rec in recommendedChallenges"
                :key="rec.challengeId"
                :id="rec.challengeId"
                :title="rec.challengeName"
                :videoUrl="rec.challengeUrl"
                class="mb-3"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 에러 상태 표시 -->
      <div v-else class="text-center my-5 py-5">
        <h2 class="text-danger">챌린지를 찾을 수 없습니다</h2>
        <p class="text-muted mt-3">요청하신 챌린지가 존재하지 않거나 삭제되었을 수 있습니다.</p>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
/**
 * @컴포넌트명: ChallengeDetail
 * @설명: 개별 챌린지의 상세 정보를 표시하는 페이지 컴포넌트
 * @데이터구조: {
 *   challenge: {
 *     id: number,
 *     title: string,
 *     description: string,
 *     videoUrl: string
 *   }
 * }
 */

import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useChallengeStore } from '@/stores/challenge';
import { useCommentStore } from '@/stores/comment';
import MainLayout from '@/layouts/MainLayout.vue';
import RecommendedChallengeCard from '@/components/RecommendedChallengeCard.vue';
import Comments from '@/components/Comments.vue';

// 상태 관리 및 라우팅 설정
const router = useRouter();
const route = useRoute();
const challengeStore = useChallengeStore();
const commentStore = useCommentStore();

// 로컬 상태 관리
const challenge = ref(null);
const loading = ref(true);
const challengeId = parseInt(route.params.id);

/**
 * YouTube URL 변환 함수
 * 일반 YouTube URL을 임베드 URL로 변환
 */
const transformYoutubeUrl = (url) => {
  if (!url) return '';
  if (url.includes('youtu.be/')) {
    const videoId = url.split('youtu.be/')[1];
    return `https://www.youtube.com/embed/${videoId}`;
  } else if (url.includes('youtube.com/watch?v=')) {
    const videoId = url.split('v=')[1];
    return `https://www.youtube.com/embed/${videoId}`;
  }
  return url;
};

// 컴포넌트 마운트 시 데이터 로드
onMounted(async () => {
  try {
    loading.value = true;
    // 챌린지 상세 정보와 메인 페이지 데이터 병렬 로드
    const [challengeResponse, mainPageResponse] = await Promise.all([
      challengeStore.fetchChallengeDetails(challengeId),
      challengeStore.fetchMainPageData()
    ]);

    challenge.value = {
      id: challengeResponse.challengeId,
      title: challengeResponse.challengeName,
      description: challengeResponse.challengeDesc,
      videoUrl: transformYoutubeUrl(challengeResponse.challengeUrl)
    };
  } catch (error) {
    console.error('Failed to fetch challenge details:', error);
  } finally {
    loading.value = false;
  }
});

// 챌린지 시작 핸들러
const startChallenge = () => {
  router.push(`/challenges/${challengeId}/score`);
};

// 추천 챌린지 목록 계산
const recommendedChallenges = computed(() => {
  return challengeStore.challenges
    .filter(c => c.challengeId !== challengeId)
    .slice(0, 10); // 10개의 추천 챌린지만 표시
});
</script>

<style scoped>
/*
  스타일링 규칙:
  - 반응형 레이아웃 (Bootstrap grid system 활용)
  - 스크롤 가능한 추천 챌린지 목록
  - 호버 효과가 있는 버튼
  - 미디어 쿼리로 모바일 대응
*/

.challenge-detail {
  margin-top: 1rem;
  margin-bottom: 2rem;
}

.content-section {
  background: white;
  border-radius: 12px;
  border: 1px solid var(--tertiary-color);
  overflow: hidden;
}

.challenge-info {
  padding: 1.5rem;
  background: white;
}

.title-action-container {
  gap: 1rem;
}

.challenge-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--primary-color);
  margin: 0;
  flex: 1;
}

.description-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 0.75rem;
}

.description-text {
  font-size: 1rem;
  line-height: 1.6;
  color: #555;
  white-space: pre-line;
}

.video-container {
  width: 100%;
  background: var(--secondary-color);
  border-radius: 8px 8px 0 0;
  overflow: hidden;
}

.video-wrapper {
  position: relative;
  padding-bottom: 56.25%;
  height: 0;
  overflow: hidden;
}

.video-wrapper iframe {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border: 0;
}

.btn-primary {
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
  background-color: var(--primary-color);
  border: none;
  white-space: nowrap;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
}

.btn-primary:hover {
  transform: translateY(-2px);
  background-color: var(--interaction-hover-color);
  border: none;
}

.recommended-challenges {
  position: sticky;
  top: 2rem;
  background: white;
  padding: 1rem;
  border-radius: 12px;
  /* border: 1px solid var(--tertiary-color); */
}

.recommended-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid var(--primary-color);
}

.recommended-list {
  max-height: calc(100vh - 250px);
  overflow-y: auto;
  padding-right: 0.5rem;
}

.recommended-list::-webkit-scrollbar {
  width: 6px;
}

.recommended-list::-webkit-scrollbar-thumb {
  background-color: var(--primary-color);
  border-radius: 3px;
}

.recommended-list::-webkit-scrollbar-track {
  background-color: var(--tertiary-color);
  border-radius: 3px;
}

@media (max-width: 992px) {
  .recommended-challenges {
    position: static;
    margin-top: 1rem;
  }

  .challenge-title {
    font-size: 1.6rem;
  }

  .description-title {
    font-size: 1.2rem;
  }
}

@media (max-width: 768px) {
  .title-action-container {
    flex-direction: column;
    gap: 1rem;
  }

  .challenge-title {
    text-align: center;
  }

  .btn-primary {
    width: 100%;
  }
}
</style>
