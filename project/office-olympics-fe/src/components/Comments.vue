<template>
  <div class="comments-section">
    <div v-if="commentStore.error" class="alert alert-danger rounded-3">
      {{ commentStore.error }}
    </div>

    <div v-if="commentStore.loading" class="text-center py-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">로딩중...</span>
      </div>
    </div>

    <div class="comments-header d-flex align-items-center mb-4">
      <h3 class="comments-title">댓글 <span class="comment-count">({{ comments.length }})</span></h3>
    </div>

    <div class="comment-form mb-4">
      <div class="d-flex gap-3">
        <div class="flex-grow-1">
          <input
            type="text"
            class="form-control comment-input"
            v-model="newComment"
            placeholder="댓글을 작성해주세요..."
          >
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

    <div class="comments-list">
      <div v-if="!commentStore.loading">
        <div v-if="!comments.length" class="text-center text-muted">
          아직 댓글이 없습니다. 첫 댓글을 작성해보세요!
        </div>

        <div v-else v-for="comment in comments.filter(c => c.commentDepth === 0)" :key="comment.commentId" class="comment-item">
          <div class="d-flex gap-3">
            <div class="comment-avatar">
              <div class="avatar-circle">
                <img
                  v-if="comment.imgSrc"
                  :src="comment.imgSrc"
                  :alt="comment.nickname"
                  class="avatar-image"
                />
                <span v-else>{{ comment.nickname?.charAt(0) || 'U' }}</span>
              </div>
            </div>
            <div class="flex-grow-1">
              <div class="comment-header">
                <div class="d-flex justify-content-between align-items-center">
                  <div>
                    <span class="comment-author">{{ comment.nickname }}</span>
                    <span class="comment-date">{{ formatDate(comment.regDate) }}</span>
                  </div>
                  <div class="dropdown comment-menu">
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

              <p class="comment-text mb-2" :class="{ 'text-muted': comment.commentText === '삭제된 댓글입니다.' }">
                {{ comment.commentText }}
              </p>

              <div class="comment-actions-bar">
                <div class="actions-left">
                  <button
                    class="btn btn-sm btn-link text-secondary"
                    @click="toggleReplyForm(comment.commentId)"
                    v-if="comment.commentDepth === 0"
                  >
                    답글
                  </button>

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

              <div v-if="editingComment && editingComment.commentId === comment.commentId" class="edit-form mt-2">
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

              <div v-if="showReplyForm === comment.commentId" class="reply-form mt-3">
                <div class="d-flex gap-3">
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
                  <div class="flex-grow-1">
                    <input
                      type="text"
                      class="form-control comment-input"
                      v-model="replyText"
                      placeholder="답글을 작성해주세요..."
                    >
                    <div class="comment-actions mt-2" v-if="replyText.trim()">
                      <div class="d-flex justify-content-end">
                        <button class="btn btn-primary me-2" @click="addReply(comment.commentId)" :disabled="!replyText.trim()">
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

              <div
                class="replies mt-3"
                v-if="expandedComments.has(comment.commentId) && getRepliesForComment(comment.commentId).length > 0"
              >
                <div
                  v-for="reply in getRepliesForComment(comment.commentId)"
                  :key="reply.commentId"
                  class="reply-item"
                >
                  <div class="d-flex gap-3">
                    <div class="comment-avatar">
                      <div class="avatar-circle">
                        <img
                          v-if="reply.imgSrc"
                          :src="reply.imgSrc"
                          :alt="reply.nickname"
                          class="avatar-image"
                        />
                        <span v-else>{{ reply.nickname?.charAt(0) || 'U' }}</span>
                      </div>
                    </div>
                    <div class="flex-grow-1">
                      <div class="comment-header">
                        <div class="d-flex justify-content-between align-items-center">
                          <div>
                            <span class="comment-author">{{ reply.nickname }}</span>
                            <span class="comment-date">{{ formatDate(reply.regDate) }}</span>
                          </div>
                          <div class="dropdown comment-menu">
                            <button class="btn btn-link btn-sm p-0" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                                <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"/>
                              </svg>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end">
                              <li><button class="dropdown-item" @click="editComment(reply)">수정</button></li>
                              <li><button class="dropdown-item text-danger" @click="deleteComment(reply.commentId)">삭제</button></li>
                            </ul>
                          </div>
                        </div>
                      </div>

                      <p class="comment-text mb-2">{{ reply.commentText }}</p>
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
import { ref, onMounted, computed, watch } from 'vue';
import { useCommentStore } from '@/stores/comment';
import { useAuthStore } from '@/stores/auth'; // Assuming you have an auth store

const props = defineProps({
  challengeId: {
    type: Number,
    required: true
  }
});

const commentStore = useCommentStore();
const authStore = useAuthStore();

const newComment = ref('');
const replyText = ref('');
const showReplyForm = ref(null);
const editingComment = ref(null);

const comments = computed(() => {
  console.log('All comments:', commentStore.comments);
  return commentStore.comments || [];
});

// Computed property to get replies for a specific comment
const getRepliesForComment = computed(() => {
  return (commentId) => {
    const parentComment = commentStore.comments.find(c => c.commentId === commentId);
    if (!parentComment) return [];

    return commentStore.comments.filter(comment =>
      comment.commentDepth === 1 &&
      (comment.commentGroup === parentComment.commentGroup || comment.commentGroup === parentComment.commentId)
    );
  };
});

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const toggleReplyForm = (commentId) => {
  showReplyForm.value = showReplyForm.value === commentId ? null : commentId;
  replyText.value = '';
};

const addComment = async () => {
  if (!newComment.value.trim()) return;

  try {
    // Optimistic update
    const tempComment = {
      commentId: Date.now(), // 임시 ID
      commentText: newComment.value.trim(),
      commentDepth: 0,
      commentGroup: Date.now(), // 임시 그룹 ID
      nickname: authStore.user?.nickname,
      imgSrc: authStore.user?.imgSrc,
      regDate: new Date().toISOString(),
      userId: authStore.user?.id
    };

    // 임시 댓글 추가
    commentStore.comments = [...commentStore.comments, tempComment];
    const commentText = newComment.value.trim();
    newComment.value = '';

    // 실제 API 호출
    const response = await commentStore.addComment(props.challengeId, {
      commentText: commentText
    });

    // 실제 댓글로 임시 댓글 교체
    if (response) {
      commentStore.comments = commentStore.comments.map(comment =>
        comment.commentId === tempComment.commentId ? response : comment
      );
    }
  } catch (error) {
    console.error('Failed to add comment:', error);
    // 에러 메시지를 사용자에게 표시하는 로직 추가 필요
    commentStore.setError('댓글 작성에 실패했습니다.');
    await commentStore.fetchComments(props.challengeId);
  }
};

const addReply = async (parentCommentId) => {
  if (!replyText.value.trim()) return;

  try {
    const parentComment = commentStore.comments.find(c => c.commentId === parentCommentId);
    if (!parentComment) return;

    // Optimistic update
    const tempReply = {
      commentId: Date.now(),
      commentText: replyText.value.trim(),
      commentDepth: 1,
      commentGroup: parentComment.commentGroup || parentComment.commentId,
      nickname: authStore.user?.nickname,
      imgSrc: authStore.user?.imgSrc,
      regDate: new Date().toISOString(),
      userId: authStore.user?.id
    };

    // 임시 답글 추가
    commentStore.comments = [...commentStore.comments, tempReply];

    const replyTextContent = replyText.value.trim();
    replyText.value = '';
    showReplyForm.value = null;

    // API 호출
    const response = await commentStore.addReply(props.challengeId, parentCommentId, {
      commentText: replyTextContent
    });

    // 실제 답글로 임시 답글 교체
    if (response) {
      await commentStore.fetchComments(props.challengeId);
    }
  } catch (error) {
    console.error('Failed to add reply:', error);
    await commentStore.fetchComments(props.challengeId);
  }
};

const deleteComment = async (commentId) => {
  try {
    await commentStore.deleteComment(props.challengeId, commentId);
  } catch (error) {
    console.error('Failed to delete comment:', error);
  }
};

const editComment = (comment) => {
  if (comment.userId !== authStore.user?.id) {
    commentStore.setError('본인이 작성한 댓글만 수정할 수 있습니다.');
    return;
  }
  editingComment.value = { ...comment };
};

const updateComment = async () => {
  if (!editingComment.value) return;

  try {
    const commentData = { commentText: editingComment.value.commentText };
    if (editingComment.value.commentDepth === 0) {
      await commentStore.updateComment(
        props.challengeId,
        editingComment.value.commentId,
        commentData
      );
    } else {
      await commentStore.updateReply(
        props.challengeId,
        editingComment.value.commentGroup,
        editingComment.value.commentId,
        commentData
      );
    }
    editingComment.value = null;
  } catch (error) {
    throw error;
  }
};

onMounted(async () => {
  console.log('Comments component mounted for challenge:', props.challengeId);
  await commentStore.fetchComments(props.challengeId);
  console.log('Comments fetched:', commentStore.comments);

  // Log the structure of each comment
  commentStore.comments.forEach(comment => {
    console.log('Comment structure:', {
      id: comment.commentId,
      depth: comment.commentDepth,
      group: comment.commentGroup,
      text: comment.commentText
    });
  });

  // 답글이 있는 댓글들의 답글 목록을 자동으로 펼침
  commentStore.comments.forEach(comment => {
    if (comment.commentDepth === 0) {
      const hasReplies = commentStore.comments.some(reply =>
        reply.commentDepth === 1 && reply.commentGroup === comment.commentGroup
      );
      if (hasReplies) {
        expandedComments.value.add(comment.commentId);
      }
    }
  });
});

// Add a watch to monitor changes
watch(() => commentStore.comments, (newComments) => {
  console.log('Comments updated:', newComments);
}, { deep: true });

// Add this with your other refs
const expandedComments = ref(new Set());

// Add this function to toggle reply visibility
const toggleReplies = (commentId) => {
  if (expandedComments.value.has(commentId)) {
    expandedComments.value.delete(commentId);
  } else {
    expandedComments.value.add(commentId);
  }
};
</script>

<style scoped>
.comments-section {
  background: white;
  border-radius: 12px;
  padding: 2rem;
}

.comments-header {
  border-bottom: 2px solid var(--primary-color);
  padding-bottom: 1rem;
}

.comments-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.comment-count {
  color: var(--primary-color);
  font-size: 1.2rem;
  font-weight: normal;
}

/* .comment-form {
  background: var(--tertiary-color);
  padding: 1.5rem;
  border-radius: 8px;
} */

.comment-input {
  background: var(--tertiary-color);
  border-radius: 8px;
  padding: 1rem 1rem;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.comment-input:focus {
  border-color: var(--primary-color);
  outline: none;
  background: white;
}

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

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.comment-item {
  padding: 1rem;
  border-bottom: 1px solid var(--tertiary-color);
  transition: background-color 0.2s ease;
}

.comment-header {
  margin-bottom: 0.5rem;
}

.comment-author {
  font-weight: 600;
  color: var(--primary-color);
  margin-right: 0.5rem;
}

.comment-date {
  font-size: 0.875rem;
  color: #6c757d;
}

.comment-text {
  font-size: 1rem;
  line-height: 1.6;
  color: #333;
}

.comment-actions button {
  font-size: 0.875rem;
  padding: 0.375rem 1rem;
}

.btn-primary {
  background-color: var(--primary-color);
  color: white;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background-color: var(--interaction-hover-color);
}

.btn-outline-secondary {
  border: 1px solid var(--tertiary-color);
  color: #333;
  background-color: white;
}

.btn-outline-secondary:hover {
  background-color: var(--secondary-color);
}

.reply-form {
  /* 기존 스타일 제거 */
  /* background: var(--tertiary-color);
  padding: 1.5rem;
  border-radius: 8px;
  margin-top: 1rem; */
}

.replies {
  margin-left: 2rem;
  padding-left: 1rem;
  border-left: 2px solid var(--tertiary-color);
}

.reply-item {
  padding: 0.75rem 0;
}

.comment-menu .dropdown-menu {
  min-width: 120px;
  padding: 0.5rem 0;
  border: 1px solid var(--tertiary-color);

}

.dropdown-item {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;

}

.dropdown-item:hover {
  background-color: var(--secondary-color);
  transform: none !important;
}

@media (max-width: 768px) {
  .comments-section {
    padding: 1rem;
  }

  .comment-form {
    padding: 1rem;
  }

  .replies {
    margin-left: 1.5rem;
    padding-left: 0.75rem;
  }
}
</style>
