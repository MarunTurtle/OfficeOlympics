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

    <div v-if="commentStore.loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <h3 class="comments-header">
      Comments <span class="comment-count">{{ comments.length }}</span>
    </h3>

    <!-- Add Comment Form -->
    <div class="comment-form mb-4">
      <div class="d-flex gap-3">
        <div class="comment-avatar">
          <!-- Placeholder avatar if no profile image -->
          <div class="avatar-placeholder">
            {{ authStore.user?.nickname?.charAt(0) || 'U' }}
          </div>
        </div>
        <div class="flex-grow-1">
          <input
            type="text"
            class="form-control comment-input"
            v-model="newComment"
            placeholder="Add a comment..."
          >
          <div class="comment-actions mt-2" v-if="newComment.trim()">
            <button
              class="btn btn-secondary me-2"
              @click="newComment = ''"
            >
              Cancel
            </button>
            <button
              class="btn btn-primary"
              @click="addComment"
              :disabled="!newComment.trim()"
            >
              Comment
            </button>
          </div>
        </div>
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
          <div class="d-flex gap-3">
            <div class="comment-avatar">
              <img v-if="comment.profileImg" :src="comment.profileImg" alt="Profile" class="avatar-img">
              <div v-else class="avatar-placeholder">
                {{ comment.nickname?.charAt(0) || 'U' }}
              </div>
            </div>
            <div class="flex-grow-1">
              <div class="comment-header">
                <span class="comment-author">{{ comment.nickname }}</span>
                <span class="comment-date">{{ formatDate(comment.regDate) }}</span>
              </div>

              <p class="comment-text" :class="{ 'text-muted': comment.commentText === '삭제된 댓글입니다.' }">
                {{ comment.commentText }}
              </p>

              <!-- Comment Actions -->
              <div class="comment-toolbar">
                <button
                  class="btn btn-sm btn-link"
                  @click="toggleReplyForm(comment.commentId)"
                  v-if="comment.commentDepth === 0"
                >
                  <i class="fas fa-reply me-1"></i>Reply
                </button>

                <div class="dropdown comment-menu" v-if="currentUserId === comment.userId">
                  <button class="btn btn-link btn-sm p-0 three-dots" type="button" data-bs-toggle="dropdown">
                    <span class="dot"></span>
                    <span class="dot"></span>
                    <span class="dot"></span>
                  </button>
                  <ul class="dropdown-menu dropdown-menu-end">
                    <li>
                      <button class="dropdown-item" @click="editComment(comment)">Edit</button>
                    </li>
                    <li>
                      <button class="dropdown-item text-danger" @click="deleteComment(comment.commentId)">Delete</button>
                    </li>
                  </ul>
                </div>
              </div>

              <!-- Edit Form -->
              <div v-if="editingComment && editingComment.commentId === comment.commentId" class="edit-form mt-2">
                <input
                  type="text"
                  class="form-control"
                  v-model="editingComment.commentText"
                >
                <div class="edit-actions mt-2">
                  <button class="btn btn-secondary btn-sm me-2" @click="editingComment = null">Cancel</button>
                  <button class="btn btn-primary btn-sm" @click="updateComment">Save</button>
                </div>
              </div>

              <!-- Reply Form -->
              <div class="reply-form mt-2" v-if="showReplyForm === comment.commentId">
                <div class="d-flex gap-3">
                  <div class="comment-avatar">
                    <div class="avatar-placeholder">
                      {{ authStore.user?.nickname?.charAt(0) || 'U' }}
                    </div>
                  </div>
                  <div class="flex-grow-1">
                    <input
                      type="text"
                      class="form-control"
                      v-model="replyText"
                      placeholder="Write a reply..."
                    >
                    <div class="reply-actions mt-2">
                      <button class="btn btn-secondary btn-sm me-2" @click="toggleReplyForm(null)">Cancel</button>
                      <button
                        class="btn btn-primary btn-sm"
                        @click="addReply(comment.commentId)"
                        :disabled="!replyText.trim()"
                      >
                        Reply
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Replies -->
              <div class="replies mt-3" v-if="comment.commentDepth === 0">
                <div
                  v-for="reply in getRepliesForComment(comment.commentId)"
                  :key="reply.commentId"
                  class="reply-item"
                >
                  <div class="d-flex gap-3">
                    <div class="comment-avatar">
                      <img v-if="reply.profileImg" :src="reply.profileImg" alt="Profile" class="avatar-img">
                      <div v-else class="avatar-placeholder">
                        {{ reply.nickname?.charAt(0) || 'U' }}
                      </div>
                    </div>
                    <div class="flex-grow-1">
                      <div class="comment-header">
                        <span class="comment-author">{{ reply.nickname }}</span>
                        <span class="comment-date">{{ formatDate(reply.regDate) }}</span>
                      </div>

                      <p class="comment-text">{{ reply.commentText }}</p>

                      <!-- Reply Actions -->
                      <div class="comment-toolbar">
                        <div class="dropdown comment-menu" v-if="reply.userId === currentUserId">
                          <button class="btn btn-link btn-sm dropdown-toggle no-arrow" type="button" data-bs-toggle="dropdown">
                            <i class="fas fa-ellipsis-v"></i>
                          </button>
                          <ul class="dropdown-menu dropdown-menu-end">
                            <li><button class="dropdown-item" @click="editComment(reply)">Edit</button></li>
                            <li><button class="dropdown-item text-danger" @click="deleteComment(reply.commentId)">Delete</button></li>
                          </ul>
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
  console.log('Current user ID:', authStore.userId);
  return authStore.userId;
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

.comment-menu .btn-link {
  color: #666;
  padding: 4px 8px;
}

.comment-menu .btn-link:hover {
  color: var(--primary-color);
}

.dropdown-item i {
  width: 16px;
}

.fa-ellipsis-vertical {
  font-size: 1.2rem;
}

.three-dots {
  display: flex;
  gap: 3px;
  align-items: center;
  padding: 4px 8px !important;
}

.dot {
  width: 4px;
  height: 4px;
  background-color: #666;
  border-radius: 50%;
  display: inline-block;
}

.three-dots:hover .dot {
  background-color: var(--primary-color);
}

.comment-menu {
  opacity: 0;
  transition: opacity 0.2s ease;
}

.comment-item:hover .comment-menu,
.reply-item:hover .comment-menu {
  opacity: 1;
}
</style>
