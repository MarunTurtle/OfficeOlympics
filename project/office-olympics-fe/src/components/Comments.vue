<template>
  <!-- 전체 댓글 섹션을 감싸는 컨테이너 -->
  <div class="comments-section">
    <!-- 에러 발생 시 표시되는 경고 메시지 -->
    <div v-if="commentStore.error" class="alert alert-danger rounded-3">
      {{ commentStore.error }}
    </div>

    <!-- 로딩 중일 때 표시되는 스피너 -->
    <div v-if="commentStore.loading" class="text-center py-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">로딩중...</span>
      </div>
    </div>

    <!-- 댓글 섹션 헤더: 제목과 댓글 수 표시 -->
    <div class="comments-header d-flex align-items-center mb-4">
      <h3 class="comments-title">댓글 <span class="comment-count">({{ comments.length }})</span></h3>
    </div>

    <!-- 새 댓글 작성 폼 -->
    <div class="comment-form mb-4">
      <div class="d-flex gap-3">
        <div class="flex-grow-1">
          <!-- 댓글 입력 필드 -->
          <input
            type="text"
            class="form-control comment-input"
            v-model="newComment"
            placeholder="댓글을 작성해주세요..."
          >
          <!-- 댓글 작성 버튼들 (댓글이 있을 때만 표시) -->
          <div class="comment-actions mt-2" v-if="newComment.trim()">
            <div class="d-flex justify-content-end">
              <button class="btn btn-primary me-2" @click="addComment" :disabled="!newComment.trim()">
                댓글 작성
              </button>
              <button class="btn btn-outline-secondary" @click="newComment = ''">
                취소
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 댓글 목록 컨테이너 -->
    <div class="comments-list">
      <div v-if="!commentStore.loading">
        <!-- 댓글이 없을 때 표시되는 메시지 -->
        <div v-if="!comments.length" class="text-center text-muted">
          아직 댓글이 없습니다. 첫 댓글을 작성해보세요!
        </div>

        <!-- 댓글 목록 (depth가 0인 최상위 댓글만 표시) -->
        <div v-else v-for="comment in comments.filter(c => c.commentDepth === 0)" :key="comment.commentId" class="comment-item">
          <div class="d-flex gap-3">
            <!-- 댓글 작성자 아바타 -->
            <div class="comment-avatar">
              <div class="avatar-circle">
                <img
                  v-if="comment.imgSrc"
                  :src="comment.imgSrc"
                  :alt="comment.nickname"
                  class="avatar-image"
                />
                <!-- 이미지가 없을 경우 닉네임 첫 글자 표시 -->
                <span v-else>{{ comment.nickname?.charAt(0) || 'U' }}</span>
              </div>
            </div>

            <!-- 댓글 내용 영역 -->
            <div class="flex-grow-1">
              <!-- 댓글 헤더: 작성자 정보와 메뉴 -->
              <div class="comment-header">
                <div class="d-flex justify-content-between align-items-center">
                  <!-- 작성자 정보 -->
                  <div>
                    <span class="comment-author">{{ comment.nickname }}</span>
                    <span class="comment-date">{{ formatDate(comment.regDate) }}</span>
                  </div>

                  <!-- 댓글 수정/삭제 드롭다운 메뉴 (작성자만 표시) -->
                  <div class="dropdown comment-menu" v-if="authStore.user && comment.userId === authStore.user.id">
                    <button class="btn btn-link btn-sm p-0" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                        <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"/>
                      </svg>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end">
                      <li><button class="dropdown-item" @click="editComment(comment)">수정</button></li>
                      <li><button class="dropdown-item text-danger" @click="deleteComment(comment.commentId)">삭제</button></li>
                    </ul>
                  </div>
                </div>
              </div>

              <!-- 댓글 본문 텍스트 (삭제된 댓글일 경우 스타일 변경) -->
              <p class="comment-text mb-2"
                 :class="{ 'text-muted': comment.commentText === '삭제된 댓글입니다.' }">
                {{ comment.commentText }}
              </p>

              <!-- 댓글 액션 바 (답글 버튼 등) -->
              <div class="comment-actions-bar">
                <div class="actions-left">
                  <!-- 답글 작성 토글 버튼 (최상위 댓글만 표시) -->
                  <button
                    class="btn btn-sm btn-link text-secondary"
                    @click="toggleReplyForm(comment.commentId)"
                    v-if="comment.commentDepth === 0"
                  >
                    답글
                  </button>

                  <!-- 답글 목록 토글 버튼 (답글이 있는 경우만 표시) -->
                  <button
                    v-if="comment.commentDepth === 0"
                    class="btn btn-sm btn-link text-secondary replies-toggle"
                    @click="toggleReplies(comment.commentId)"
                  >
                    <span v-if="getRepliesForComment(comment.commentId).length > 0">
                      {{ expandedComments.has(comment.commentId) ? '답글 숨기기' : '답글 보기' }}
                      ({{ getRepliesForComment(comment.commentId).length }})
                    </span>
                  </button>
                </div>
              </div>

              <!-- 댓글 수정 폼 (수정 모드일 때만 표시) -->
              <div v-if="editingComment && editingComment.commentId === comment.commentId"
                   class="edit-form mt-2">
                <input
                  type="text"
                  class="form-control"
                  v-model="editingComment.commentText"
                >
                <div class="edit-actions mt-2">
                  <div class="d-flex justify-content-end">
                    <button class="btn btn-primary btn-sm me-2" @click="updateComment">저장</button>
                    <button class="btn btn-secondary btn-sm" @click="editingComment = null">취소</button>
                  </div>
                </div>
              </div>

              <!-- 답글 작성 폼 (답글 작성 모드일 때만 표시) -->
              <div v-if="showReplyForm === comment.commentId" class="reply-form mt-3">
                <div class="d-flex gap-3">
                  <!-- 답글 작성자 아바타 -->
                  <div class="comment-avatar">
                    <div class="avatar-circle">
                      <img
                        v-if="authStore.user?.imgSrc"
                        :src="authStore.user.imgSrc"
                        :alt="authStore.user?.nickname"
                        class="avatar-image"
                      />
                      <span v-else>{{ authStore.user?.nickname?.charAt(0) || 'U' }}</span>
                    </div>
                  </div>
                  <!-- 답글 입력 영역 -->
                  <div class="flex-grow-1">
                    <input
                      type="text"
                      class="form-control comment-input"
                      v-model="replyText"
                      placeholder="답글을 작성해주세요..."
                    >
                    <!-- 답글 작성 버튼 (텍스트가 있을 때만 표시) -->
                    <div class="comment-actions mt-2" v-if="replyText.trim()">
                      <div class="d-flex justify-content-end">
                        <button
                          class="btn btn-primary me-2"
                          @click="addReply(comment.commentId)"
                          :disabled="!replyText.trim()"
                        >
                          답글 작성
                        </button>
                        <button class="btn btn-outline-secondary" @click="toggleReplyForm(null)">
                          취소
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 답글 목록 섹션 (펼침 상태이고 답글이 있을 때만 표시) -->
              <div
                class="replies mt-3"
                v-if="expandedComments.has(comment.commentId) && getRepliesForComment(comment.commentId).length > 0"
              >
                <!-- 개별 답글 아이템 -->
                <div
                  v-for="reply in getRepliesForComment(comment.commentId)"
                  :key="reply.commentId"
                  class="reply-item"
                >
                  <div class="d-flex gap-3">
                    <!-- 답글 작성자 아바타 -->
                    <div class="comment-avatar">
                      <div class="avatar-circle">
                        <img
                          v-if="reply.imgSrc"
                          :src="reply.imgSrc"
                          :alt="reply.nickname"
                          class="avatar-image"
                        />
                        <!-- 이미지가 없을 경우 닉네임 첫 글자 표시 -->
                        <span v-else>{{ reply.nickname?.charAt(0) || 'U' }}</span>
                      </div>
                    </div>

                    <!-- 답글 내용 영역 -->
                    <div class="flex-grow-1">
                      <!-- 답글 헤더: 작성자 정보와 메뉴 -->
                      <div class="comment-header">
                        <div class="d-flex justify-content-between align-items-center">
                          <!-- 답글 작성자 정보 -->
                          <div>
                            <span class="comment-author">{{ reply.nickname }}</span>
                            <span class="comment-date">{{ formatDate(reply.regDate) }}</span>
                          </div>

                          <!-- 답글 수정/삭제 드롭다운 메뉴 (작성자만 표시) -->
                          <div class="dropdown comment-menu"
                               v-if="authStore.user && reply.userId === authStore.user.id">
                            <button class="btn btn-link btn-sm p-0"
                                    type="button"
                                    data-bs-toggle="dropdown"
                                    aria-expanded="false">
                              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                                <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"/>
                              </svg>
                            </button>
                            <!-- 답글 수정/삭제 메뉴 아이템 -->
                            <ul class="dropdown-menu dropdown-menu-end">
                              <li><button class="dropdown-item" @click="editComment(reply)">수정</button></li>
                              <li><button class="dropdown-item text-danger" @click="deleteComment(reply.commentId)">삭제</button></li>
                            </ul>
                          </div>
                        </div>
                      </div>

                      <!-- 답글 본문 텍스트 (삭제된 답글일 경우 스타일 변경) -->
                      <p class="comment-text mb-2"
                         :class="{ 'text-muted': reply.commentText === '삭제된 댓글입니다.' }">
                        {{ reply.commentText }}
                      </p>

                      <!-- 답글 수정 폼 (수정 모드일 때만 표시) -->
                      <div v-if="editingComment && editingComment.commentId === reply.commentId"
                           class="edit-form mt-2">
                        <!-- 답글 수정 입력 필드 -->
                        <input
                          type="text"
                          class="form-control"
                          v-model="editingComment.commentText"
                        >
                        <!-- 답글 수정 버튼 그룹 -->
                        <div class="edit-actions mt-2">
                          <div class="d-flex justify-content-end">
                            <button class="btn btn-primary btn-sm me-2" @click="updateComment">저장</button>
                            <button class="btn btn-secondary btn-sm" @click="editingComment = null">취소</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 필요한 Vue 컴포저블과 스토어들을 가져옴
import { ref, onMounted, computed, watch } from 'vue';
import { useCommentStore } from '@/stores/comment';
import { useAuthStore } from '@/stores/auth';

// 부모 컴포넌트로부터 챌린지 ID를 전달받음
const props = defineProps({
  challengeId: {
    type: Number,
    required: true
  }
});

// 스토어 초기화
const commentStore = useCommentStore();
const authStore = useAuthStore();

// 댓글 관련 상태 관리
const newComment = ref('');      // 새 댓글 입력값
const replyText = ref('');       // 답글 입력값
const showReplyForm = ref(null); // 답글 폼 표시 여부
const editingComment = ref(null); // 수정 중인 댓글 정보

// 전체 댓글 목록을 반환하는 computed 속성
const comments = computed(() => {
  console.log('All comments:', commentStore.comments);
  return commentStore.comments || [];
});

// 특정 댓글에 대한 답글 목록을 반환하는 computed 속성
const getRepliesForComment = computed(() => {
  return (commentId) => {
    // 부모 댓글 찾기
    const parentComment = commentStore.comments.find(c => c.commentId === commentId);
    if (!parentComment) return [];

    // 해당 부모 댓글에 속한 답글들만 필터링
    return commentStore.comments.filter(comment =>
      comment.commentDepth === 1 && // depth가 1인 답글만 선택
      (comment.commentGroup === parentComment.commentGroup || comment.commentGroup === parentComment.commentId)
    );
  };
});

/**
 * 날짜를 한국어 형식으로 포맷팅하는 함수
 * @param {Date} date - 포맷팅할 날짜
 * @returns {string} 포맷팅된 날짜 문자열 (예: 2024년 3월 15일 오후 2:30)
 */
const formatDate = (date) => {
  return new Date(date).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

/**
 * 답글 작성 폼의 표시 상태를 토글하는 함수
 * @param {number} commentId - 토글할 댓글의 ID
 */
const toggleReplyForm = (commentId) => {
  showReplyForm.value = showReplyForm.value === commentId ? null : commentId;
  replyText.value = '';
};

/**
 * 새로운 댓글을 추가하는 비동기 함수
 * Optimistic UI 패턴을 사용하여 즉각적인 UI 업데이트 제공
 */
const addComment = async () => {
  if (!newComment.value.trim()) return;

  try {
    // 임시 댓글 객체 생성
    const tempComment = {
      commentId: Date.now(),
      commentText: newComment.value.trim(),
      commentDepth: 0,
      commentGroup: Date.now(),
      nickname: authStore.user?.nickname,
      imgSrc: authStore.user?.imgSrc,
      regDate: new Date().toISOString(),
      userId: authStore.user?.id
    };

    // UI를 즉시 업데이트하여 사용자 경험 향상
    commentStore.comments = [...commentStore.comments, tempComment];
    const commentText = newComment.value.trim();
    newComment.value = '';

    // 서버에 실제 댓글 저장 요청
    const response = await commentStore.addComment(props.challengeId, {
      commentText: commentText
    });

    // 서버 응답으로 임시 댓글을 실제 댓글로 교체
    if (response) {
      commentStore.comments = commentStore.comments.map(comment =>
        comment.commentId === tempComment.commentId ? response : comment
      );
    }
  } catch (error) {
    console.error('Failed to add comment:', error);
    commentStore.setError('댓글 작성에 실패했습니다.');
    // 오류 발생 시 전체 댓글 목록 새로고침
    await commentStore.fetchComments(props.challengeId);
  }
};

/**
 * 부모 댓글에 대한 답글을 추가하는 함수
 * Optimistic UI 패턴을 사용하여 사용자 경험을 향상시킴
 *
 * @param {number} parentCommentId - 부모 댓글의 고유 식별자
 * @throws {Error} 답글 추가 실패 시 에러를 던짐
 */
const addReply = async (parentCommentId) => {
  // 빈 답글 방지
  if (!replyText.value.trim()) return;

  try {
    // 부모 댓글 존재 여부 확인
    const parentComment = commentStore.comments.find(c => c.commentId === parentCommentId);
    if (!parentComment) return;

    // 임시 답글 객체 생성 (낙관적 UI 업데이트)
    const tempReply = {
      commentId: Date.now(),
      commentText: replyText.value.trim(),
      commentDepth: 1,                        // 답글은 depth가 1
      commentGroup: parentComment.commentId,   // 부모 댓글의 ID를 그룹으로 설정
      nickname: authStore.user?.nickname,
      imgSrc: authStore.user?.imgSrc,
      regDate: new Date().toISOString(),
      userId: authStore.user?.id
    };

    // 임시 답글을 즉시 화면에 표시
    commentStore.comments = [...commentStore.comments, tempReply];

    // 입력값 초기화 및 폼 닫기
    const replyTextContent = replyText.value.trim();
    replyText.value = '';
    showReplyForm.value = null;

    // 서버에 답글 저장 요청
    const response = await commentStore.addReply(props.challengeId, parentCommentId, {
      commentText: replyTextContent
    });

    // 서버 응답 성공 시 전체 댓글 목록 새로고침
    if (response) {
      await commentStore.fetchComments(props.challengeId);
    }
  } catch (error) {
    console.error('Failed to add reply:', error);
    // 오류 발생 시 전체 댓글 목록 새로고침
    await commentStore.fetchComments(props.challengeId);
  }
};

/**
 * 댓글 삭제를 처리하는 함수
 * @param {number} commentId - 삭제할 댓글의 ID
 */
const deleteComment = async (commentId) => {
  // 삭제할 댓글을 찾고 권한 검증
  const comment = commentStore.comments.find(c => c.commentId === commentId);
  if (!comment || !authStore.user || comment.userId !== authStore.user.id) {
    commentStore.setError('본인이 작성한 댓글만 삭제할 수 있습니다.');
    return;
  }

  try {
    await commentStore.deleteComment(props.challengeId, commentId);
  } catch (error) {
    console.error('Failed to delete comment:', error);
    commentStore.setError(error.response?.data || '댓글 삭제에 실패했습니다.');
  }
};

/**
 * 댓글 수정 모드를 활성화하는 함수
 * @param {Object} comment - 수정할 댓글 객체
 */
const editComment = (comment) => {
  // 댓글 수정 권한 검증
  if (!comment || !authStore.user || comment.userId !== authStore.user.id) {
    commentStore.setError('본인이 작성한 댓글만 수정할 수 있습니다.');
    return;
  }
  // 수정할 댓글의 복사본을 생성하여 상태 업데이트
  editingComment.value = { ...comment };
};

/**
 * 댓글 수정 내용을 저장하는 함수
 * 일반 댓글과 답글을 구분하여 처리
 */
const updateComment = async () => {
  if (!editingComment.value) return;

  try {
    const commentData = { commentText: editingComment.value.commentText };

    // 일반 댓글인 경우 (depth = 0)
    if (editingComment.value.commentDepth === 0) {
      await commentStore.updateComment(
        props.challengeId,
        editingComment.value.commentId,
        commentData
      );
    }
    // 답글인 경우 (depth = 1)
    else {
      // 부모 댓글 ID와 답글 ID 설정
      const parentCommentId = editingComment.value.commentGroup;
      const replyId = editingComment.value.commentId;

      // 현재 댓글 데이터 임시 저장
      const currentComment = { ...editingComment.value };

      // 서버에 답글 수정 요청
      await commentStore.updateReply(
        props.challengeId,
        parentCommentId,
        replyId,
        commentData
      );

      // 로컬 상태 업데이트
      const updatedComments = commentStore.comments.map(comment => {
        if (comment.commentId === replyId) {
          return {
            ...comment,
            commentText: currentComment.commentText,
            updateDate: new Date().toISOString()
          };
        }
        return comment;
      });

      commentStore.comments = updatedComments;
    }
    // 수정 모드 종료
    editingComment.value = null;
  } catch (error) {
    console.error('Failed to update comment:', error);
    commentStore.setError(error.response?.data || '댓글 수정에 실패했습니다.');
  }
};

/**
 * 컴포넌트 마운트 시 실행되는 초기화 함수
 * 1. 댓글 데이터를 불러옴
 * 2. 디버깅을 위한 댓글 구조 로깅
 * 3. 답글이 있는 댓글들의 답글 목록을 자동으로 펼침
 */
onMounted(async () => {
  // 챌린지 ID에 해당하는 댓글 데이터 로드
  console.log('Comments component mounted for challenge:', props.challengeId);
  await commentStore.fetchComments(props.challengeId);
  console.log('Comments fetched:', commentStore.comments);

  // 디버깅: 각 댓글의 구조를 콘솔에 출력
  commentStore.comments.forEach(comment => {
    console.log('Comment structure:', {
      id: comment.commentId,
      depth: comment.commentDepth,
      group: comment.commentGroup,
      text: comment.commentText
    });
  });

  // 답글이 존재하는 댓글들을 자동으로 펼침 처리
  commentStore.comments.forEach(comment => {
    if (comment.commentDepth === 0) { // 최상위 댓글인 경우
      const hasReplies = commentStore.comments.some(reply =>
        reply.commentDepth === 1 && reply.commentGroup === comment.commentGroup
      );
      if (hasReplies) {
        expandedComments.value.add(comment.commentId);
      }
    }
  });
});

/**
 * 댓글 목록 변경 감지를 위한 감시자
 * 댓글 데이터가 변경될 때마다 콘솔에 기록
 */
watch(() => commentStore.comments, (newComments) => {
  console.log('Comments updated:', newComments);
}, { deep: true });

// 답글 펼침/접힘 상태를 관리하는 Set 객체
const expandedComments = ref(new Set());

/**
 * 답글 목록의 표시 상태를 토글하는 함수
 * @param {number} commentId - 토글할 댓글의 ID
 */
const toggleReplies = (commentId) => {
  if (expandedComments.value.has(commentId)) {
    expandedComments.value.delete(commentId); // 답글 목록 접기
  } else {
    expandedComments.value.add(commentId);    // 답글 목록 펼치기
  }
};
</script>

<style scoped>
/* 댓글 섹션의 기본 컨테이너 스타일 */
.comments-section {
  background: white;
  border-radius: 12px;
  padding: 2rem;
}

/* 댓글 섹션 상단 헤더 영역 스타일 */
.comments-header {
  border-bottom: 2px solid var(--primary-color);
  padding-bottom: 1rem;
}

/* 댓글 섹션 제목 스타일 */
.comments-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

/* 댓글 개수 표시 스타일 */
.comment-count {
  color: var(--primary-color);
  font-size: 1.2rem;
  font-weight: normal;
}

/* 댓글 입력 필드 기본 스타일 */
.comment-input {
  background: var(--tertiary-color);
  border-radius: 8px;
  padding: 1rem 1rem;
  font-size: 1rem;
  transition: all 0.3s ease;
}

/* 댓글 입력 필드 포커스 시 스타일 */
.comment-input:focus {
  border-color: var(--primary-color);
  outline: none;
  background: white;
}

/* 사용자 아바타 원형 컨테이너 스타일 */
.avatar-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--tertiary-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  overflow: hidden;
}

/* 아바타 이미지 스타일 */
.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 개별 댓글 아이템 스타일 */
.comment-item {
  padding: 1rem;
  border-bottom: 1px solid var(--tertiary-color);
  transition: background-color 0.2s ease;
}

/* 댓글 헤더 영역 스타일 */
.comment-header {
  margin-bottom: 0.5rem;
}

/* 댓글 작성자 이름 스타일 */
.comment-author {
  font-weight: 600;
  color: var(--primary-color);
  margin-right: 0.5rem;
}

/* 댓글 작성 날짜 스타일 */
.comment-date {
  font-size: 0.875rem;
  color: #6c757d;
}

/* 댓글 본문 텍스트 스타일 */
.comment-text {
  font-size: 1rem;
  line-height: 1.6;
  color: #333;
}

/* 댓글 액션 버튼 공통 스타일 */
.comment-actions button {
  font-size: 0.875rem;
  padding: 0.375rem 1rem;
}

/* 주요 액션 버튼 스타일 */
.btn-primary {
  background-color: var(--primary-color);
  color: white;
  font-weight: 500;
  transition: all 0.3s ease;
}

/* 주요 액션 버튼 호버 효과 */
.btn-primary:hover {
  background-color: var(--interaction-hover-color);
}

/* 보조 액션 버튼 스타일 */
.btn-outline-secondary {
  border: 1px solid var(--tertiary-color);
  color: #333;
  background-color: white;
}

/* 보조 액션 버튼 호버 효과 */
.btn-outline-secondary:hover {
  background-color: var(--secondary-color);
}

/* 답글 영역 스타일 */
.replies {
  margin-left: 2rem;
  padding-left: 1rem;
  border-left: 2px solid var(--tertiary-color);
}

/* 개별 답글 아이템 스타일 */
.reply-item {
  padding: 0.75rem 0;
}

/* 댓글 메뉴 드롭다운 스타일 */
.comment-menu .dropdown-menu {
  min-width: 120px;
  padding: 0.5rem 0;
  border: 1px solid var(--tertiary-color);
}

/* 드롭다운 메뉴 아이템 스타일 */
.dropdown-item {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
}

/* 드롭다운 메뉴 아이템 호버 효과 */
.dropdown-item:hover {
  background-color: var(--secondary-color);
  transform: none !important;
}

/* 모바일 반응형 스타일 */
@media (max-width: 768px) {
  /* 모바일에서의 댓글 섹션 패딩 조정 */
  .comments-section {
    padding: 1rem;
  }

  /* 모바일에서의 댓글 폼 패딩 조정 */
  .comment-form {
    padding: 1rem;
  }

  /* 모바일에서의 답글 영역 여백 조정 */
  .replies {
    margin-left: 1.5rem;
    padding-left: 0.75rem;
  }
}
</style>

