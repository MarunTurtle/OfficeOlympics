/**
 * @파일명: EditProfileModal.vue
 * @설명: 사용자 프로필 수정을 위한 모달 컴포넌트
 * @관련백엔드:
 *   - PUT /api/users/{userId} (프로필 업데이트)
 *   - 멀티파트 폼데이터로 이미지와 함께 전송
 */

<template>
  <!-- Bootstrap 모달 구조 -->
  <div class="modal fade" id="editProfileModal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <!-- 모달 헤더 -->
        <div class="modal-header">
          <h5 class="modal-title">프로필 수정</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>

        <!-- 모달 본문: 프로필 수정 폼 -->
        <div class="modal-body">
          <!-- prevent 수식어로 기본 폼 제출 동작 방지 -->
          <form @submit.prevent="handleSubmit">
            <!-- 닉네임 입력 필드 -->
            <div class="mb-3">
              <label class="form-label">닉네임</label>
              <input
                type="text"
                class="form-control input-field"
                v-model="formData.nickname"
                required
              >
            </div>

            <!--
              프로필 이미지 업로드 필드
              accept 속성으로 이미지 파일만 허용
            -->
            <div class="mb-3">
              <label class="form-label">프로필 이미지</label>
              <input
                type="file"
                class="form-control input-field"
                @change="handleFileChange"
                accept="image/jpeg,image/png"
              >
            </div>

            <!--
              올림픽 이름 입력 필드
              한 그룹의 대표 이름
            -->
            <div class="mb-3">
              <label class="form-label">올림픽 이름</label>
              <input
                type="text"
                class="form-control input-field"
                v-model="formData.olympicsName"
                required
              >
            </div>

            <!--
              플레이어 이름 입력 필드들
              동적으로 여러 플레이어 추가 가능
            -->
            <div class="mb-3">
              <label class="form-label">올림픽 플레이어</label>
              <div v-for="(player, index) in formData.playerNames" :key="index">
                <input
                  type="text"
                  class="form-control input-field mb-2"
                  v-model="formData.playerNames[index]"
                  :placeholder="`플레이어 ${index + 1}`"
                  required
                >
              </div>
            </div>
          </form>
        </div>

        <!--
          모달 푸터: 저장/취소 버튼
          저장 중일 때는 버튼 비활성화
        -->
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-primary"
            style="width: 48%;"
            @click="handleSubmit"
            :disabled="loading"
          >
            {{ loading ? '저장 중...' : '변경사항 저장' }}
          </button>
          <button
            type="button"
            class="btn btn-secondary cancel-button"
            style="width: 48%;"
            data-bs-dismiss="modal"
          >
            취소
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * @컴포넌트명: EditProfileModal
 * @props: {
 *   userData: 현재 사용자 정보 객체 (userId, nickname 포함)
 *   players: 현재 플레이어 목록 배열 (olympics_name, player_name 포함)
 * }
 * @emits: {
 *   update: 프로필 업데이트 성공 시 부모 컴포넌트에 알림
 * }
 */

import { ref, onMounted, watch } from 'vue';
import { Modal } from 'bootstrap';
import { useUserStore } from '@/stores/user';

// Props 정의
const props = defineProps({
  userData: {
    type: Object,
    required: true
  },
  players: {
    type: Array,
    required: true
  }
});

const emit = defineEmits(['update']);

// 상태 관리 및 참조 설정
const userStore = useUserStore();
const modal = ref(null);
const loading = ref(false);

// 폼 데이터 초기 상태
const formData = ref({
  nickname: '',
  profileImg: null,
  olympicsName: '',
  playerNames: ['']
});

// userData 변경 감지하여 폼 데이터 업데이트
watch(() => props.userData, (newData) => {
  if (newData) {
    formData.value.nickname = newData.nickname || '';
  }
}, { immediate: true });

// players 배열 변경 감지하여 폼 데이터 업데이트
watch(() => props.players, (newPlayers) => {
  if (newPlayers && newPlayers.length > 0) {
    formData.value.olympicsName = newPlayers[0].olympics_name || '';
    formData.value.playerNames = newPlayers.map(p => p.player_name);
  }
}, { immediate: true });

/**
 * 파일 입력 변경 핸들러
 * 선택된 이미지 파일을 폼데이터에 저장
 */
const handleFileChange = (event) => {
  formData.value.profileImg = event.target.files[0];
};

/**
 * 폼 제출 핸들러
 * FormData 객체를 생성하여 백엔드로 전송
 * 이미지와 텍스트 데이터를 함께 전송하기 위해 멀티파트 형식 사용
 */
const handleSubmit = async () => {
  try {
    loading.value = true;
    const submitData = new FormData();

    // 폼 데이터 구성
    submitData.append('nickname', formData.value.nickname);
    submitData.append('olympicsName', formData.value.olympicsName);
    formData.value.playerNames.forEach(name => {
      submitData.append('playerNames', name);
    });
    if (formData.value.profileImg) {
      submitData.append('profileImg', formData.value.profileImg);
    }

    // API 호출 및 후처리
    const response = await userStore.updateUser(props.userData.userId, submitData);
    modal.value.hide();
    await emit('update');
    formData.value.profileImg = null;

  } catch (error) {
    console.error('Failed to update profile:', error);
  } finally {
    loading.value = false;
  }
};

// Bootstrap 모달 초기화
onMounted(() => {
  modal.value = new Modal(document.getElementById('editProfileModal'));
});

// 외부에서 모달 제어를 위한 메서드 노출
defineExpose({
  show: () => modal.value.show(),
  hide: () => modal.value.hide()
});
</script>

<style scoped>
/*
  스타일링 규칙:
  - 모든 입력 필드는 tertiary-color 배경
  - 포커스 시 흰색 배경으로 변경
  - 버튼은 hover 시 스케일 효과
  - 모달은 부드러운 모서리 처리
*/

/* Add consistent styling */
.form-control {
  background: var(--tertiary-color);
  border-radius: 8px;
  /* padding: 0.5rem; */
  font-size: 1rem;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.form-control:focus {
  border-color: var(--primary-color);
  outline: none;
  background: white;
}

.btn-primary {
  background-color: var(--primary-color);
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background-color: var(--interaction-hover-color);
  transform: scale(1.02);
}

.btn-primary:disabled {
  background-color: #ccc;
  transform: none;
}

.btn-secondary {
  background-color: grey;
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
  color: black;
}

.btn-secondary:hover {
  background-color: darkgrey;
  transform: scale(1.02);
}

.modal-content {
  border-radius: 12px;
}

.modal-header {
  border-bottom: none;
}

.modal-footer {
  border-top: none;
  display: flex;
  justify-content: space-between;
  padding: 1rem 1rem;
}

.modal-title {
  color: var(--primary-color);
  font-weight: 600;
}

.form-label {
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.mb-3 {
  margin-bottom: 0.75rem !important;
}

.cancel-button {
  background-color: var(--tertiary-color);
}
</style>
