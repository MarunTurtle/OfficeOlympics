<template>
  <div class="comments-section">
    <!-- Add debug info -->
    <div class="debug-info">
      Loading: {{ commentStore.loading }}
      Comments length: {{ comments.length }}
    </div>

    <div v-if="commentStore.error" class="alert alert-danger">
      {{ commentStore.error }}
    </div>

    <!-- Add loading spinner -->
    <div v-if="commentStore.loading" class="text-center">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <h3>Comments</h3>

    <!-- Add Comment Form -->
    <div class="comment-form mb-4">
      <div class="input-group">
        <input
          type="text"
          class="form-control"
          v-model="newComment"
          placeholder="Add a comment..."
        >
        <button
          class="btn btn-primary btn-sm"
          @click="addComment"
          :disabled="!newComment.trim()"
          style="width: 80px;"
        >
          Post
        </button>
      </div>
    </div>

    <!-- Comments List -->
    <div class="comments-list">
      <div v-if="!commentStore.loading">
        <div v-if="!comments.length" class="text-center text-muted">
          No comments yet. Be the first to comment!
        </div>

        <div v-else v-for="comment in comments" :key="comment.commentId" class="comment-item">
          <!-- Main Comment -->
          <div class="d-flex justify-content-between align-items-start">
            <div class="comment-content">
              <div class="comment-header">
                <img :src="comment.profileImg" alt="Profile" class="profile-img" v-if="comment.profileImg">
                <strong class="comment-author">{{ comment.nickname }}</strong>
                <small class="text-muted">{{ formatDate(comment.regDate) }}</small>
              </div>
              <p class="comment-text mb-1" :class="{ 'text-muted': comment.commentText === '삭제된 댓글입니다.' }">
                {{ comment.commentText }}
              </p>

              <!-- Reply Button -->
              <button
                class="btn btn-sm btn-link"
                @click="toggleReplyForm(comment.commentId)"
                v-if="comment.commentDepth === 0"
              >
                Reply
              </button>
            </div>

            <!-- Comment Actions -->
            <div class="comment-actions" v-if="comment.userId === currentUserId">
              <button class="btn btn-sm btn-outline-primary me-2" @click="editComment(comment)">
                Edit
              </button>
              <button class="btn btn-sm btn-outline-danger" @click="deleteComment(comment.commentId)">
                Delete
              </button>
            </div>
          </div>

          <!-- Reply Form -->
          <div class="reply-form ms-4 mt-2" v-if="showReplyForm === comment.commentId">
            <div class="input-group">
              <input
                type="text"
                class="form-control"
                v-model="replyText"
                placeholder="Write a reply..."
              >
              <button
                class="btn btn-primary"
                @click="addReply(comment.commentId)"
                :disabled="!replyText.trim()"
              >
                Reply
              </button>
            </div>
          </div>

          <!-- Replies -->
          <div class="replies ms-4" v-if="comment.commentDepth === 0">
            <div
              v-for="reply in getRepliesForComment(comment.commentId)"
              :key="reply.commentId"
              class="reply-item mt-2"
            >
              <div class="d-flex justify-content-between align-items-start">
                <div class="reply-content">
                  <div class="reply-header">
                    <img :src="reply.profileImg" alt="Profile" class="profile-img" v-if="reply.profileImg">
                    <strong class="reply-author">{{ reply.nickname }}</strong>
                    <small class="text-muted">{{ formatDate(reply.regDate) }}</small>
                  </div>
                  <p class="reply-text mb-1">{{ reply.commentText }}</p>
                </div>

                <!-- Reply Actions -->
                <div class="reply-actions" v-if="reply.userId === currentUserId">
                  <button class="btn btn-sm btn-outline-primary me-2" @click="editReply(reply)">
                    Edit
                  </button>
                  <button class="btn btn-sm btn-outline-danger" @click="deleteComment(reply.commentId)">
                    Delete
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Add this inside the comment-item div, when editing -->
          <div v-if="editingComment && editingComment.commentId === comment.commentId" class="edit-form mb-2">
            <div class="input-group">
              <input
                type="text"
                class="form-control"
                v-model="editingComment.commentText"
              >
              <button class="btn btn-primary" @click="updateComment">Save</button>
              <button class="btn btn-secondary" @click="editingComment = null">Cancel</button>
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
const currentUserId = computed(() => {
  console.log('Auth store state:', authStore.$state);
  return authStore.userId || null;
});

const comments = computed(() => {
  console.log('Computing comments:', commentStore.comments);
  return commentStore.comments || [];
});

// Computed property to get replies for a specific comment
const getRepliesForComment = (commentId) => {
  return commentStore.comments.filter(
    comment => comment.commentDepth === 1 && comment.commentGroup === commentId
  );
};

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
  console.log('Current user ID:', currentUserId.value);
  console.log('Auth store state:', authStore.$state);

  if (!currentUserId.value) {
    commentStore.setError('로그인이 필요합니다.');
    return;
  }
  if (!newComment.value.trim()) return;

  try {
    await commentStore.addComment(props.challengeId, {
      commentText: newComment.value.trim()
    });
    newComment.value = '';
  } catch (error) {
    console.error('Failed to add comment:', error);
  }
};

const addReply = async (commentId) => {
  if (!currentUserId.value) {
    commentStore.setError('로그인이 필요합니다.');
    return;
  }
  if (!replyText.value.trim()) return;

  try {
    await commentStore.addReply(props.challengeId, commentId, {
      commentText: replyText.value.trim()
    });
    replyText.value = '';
    showReplyForm.value = null;
  } catch (error) {
    console.error('Failed to add reply:', error);
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
  editingComment.value = { ...comment };
};

const updateComment = async () => {
  if (!currentUserId.value || !editingComment.value) return;

  if (editingComment.value.userId !== currentUserId.value) {
    commentStore.setError('본인이 작성한 댓글만 수정할 수 있습니다.');
    return;
  }

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
    console.error('Failed to update comment:', error);
  }
};

onMounted(async () => {
  console.log('Comments component mounted');
  await commentStore.fetchComments(props.challengeId);
  console.log('Comments fetched:', commentStore.comments);
});

// Add a watch to monitor changes
watch(() => commentStore.comments, (newComments) => {
  console.log('Comments updated:', newComments);
}, { deep: true });
</script>

<style scoped>
.comments-section {
  margin-top: 30px;
}

.profile-img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: 8px;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 4px;
}

.reply-form {
  margin: 8px 0;
}

.comment-actions, .reply-actions {
  opacity: 0;
  transition: opacity 0.2s;
}

.comment-item:hover .comment-actions,
.reply-item:hover .reply-actions {
  opacity: 1;
}

/* Reuse existing styles from ChallengeDetail.vue */
.comment-form {
  margin-bottom: 2rem;
}

.comments-list {
  max-height: 500px;
  overflow-y: auto;
}

.comment-author, .reply-author {
  color: var(--primary-color);
  margin-right: 0.5rem;
}

.input-group {
  display: flex;
  gap: 8px;
}

.input-group .btn {
  border-radius: 4px;
  height: 38px;
  padding: 0 16px;
}

.input-group .form-control {
  border-radius: 4px;
}
</style>
