<template>
  <div class="comments-section">
    <div v-if="commentStore.error" class="alert alert-danger">
      {{ commentStore.error }}
    </div>

    <div v-if="commentStore.loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>

    <h3 class="comments-header mb-4">
      Comments <span class="comment-count">({{ comments.length }})</span>
    </h3>

    <!-- Add Comment Form -->
    <div class="comment-form mb-4">
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
            v-model="newComment"
            placeholder="Add a comment..."
          >
          <div class="comment-actions mt-2" v-if="newComment.trim()">
            <button class="btn btn-secondary me-2" @click="newComment = ''">
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

        <div v-else v-for="comment in comments.filter(c => c.commentDepth === 0)" :key="comment.commentId" class="comment-item">
          <!-- Main Comment -->
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
                <span class="comment-author">{{ comment.nickname }}</span>
                <span class="comment-date">{{ formatDate(comment.regDate) }}</span>
              </div>

              <p class="comment-text mb-2" :class="{ 'text-muted': comment.commentText === '삭제된 댓글입니다.' }">
                {{ comment.commentText }}
              </p>

              <!-- Comment Actions -->
              <div class="comment-actions-bar">
                <div class="actions-left">
                  <button
                    class="btn btn-sm btn-link text-secondary"
                    @click="toggleReplyForm(comment.commentId)"
                    v-if="comment.commentDepth === 0"
                  >
                    Reply
                  </button>

                  <!-- Add this new replies toggle button -->
                  <button
                    v-if="getRepliesForComment(comment.commentId).length > 0"
                    class="btn btn-sm btn-link text-secondary replies-toggle"
                    @click="toggleReplies(comment.commentId)"
                  >
                    <span>{{ getRepliesForComment(comment.commentId).length }} replies</span>
                    <i
                      class="bi bi-chevron-down"
                      :class="{ 'rotated': expandedComments.has(comment.commentId) }"
                    ></i>
                  </button>
                </div>
                <div class="dropdown comment-menu ms-auto">
                  <button class="btn btn-link btn-sm p-0" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                      <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"/>
                    </svg>
                  </button>
                  <ul class="dropdown-menu dropdown-menu-end">
                    <li><button class="dropdown-item" @click="editComment(comment)">Edit</button></li>
                    <li><button class="dropdown-item text-danger" @click="deleteComment(comment.commentId)">Delete</button></li>
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
              <div class="replies mt-3" v-if="comment.commentDepth === 0 && expandedComments.has(comment.commentId)">
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
                        <span class="comment-author">{{ reply.nickname }}</span>
                        <span class="comment-date">{{ formatDate(reply.regDate) }}</span>
                      </div>

                      <p class="comment-text mb-2">{{ reply.commentText }}</p>

                      <!-- Reply Actions -->
                      <div class="comment-actions-bar">
                        <div class="dropdown comment-menu ms-auto">
                          <button class="btn btn-link btn-sm p-0" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                              <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"/>
                            </svg>
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
    throw error;
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
  margin-top: 2rem;
}

.comments-header {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--primary-color);
}

.comment-count {
  color: #666;
  font-weight: normal;
}

.avatar-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--primary-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  overflow: hidden;
}

.comment-form {
  margin-bottom: 2rem;
}

.comment-input {
  border: none;
  border-bottom: 1px solid #dee2e6;
  border-radius: 0;
  padding: 8px 0;
}

.comment-input:focus {
  box-shadow: none;
  border-color: var(--primary-color);
}

.comment-item {
  padding: 1rem 0;
  border-bottom: 1px solid #dee2e6;
}

.comment-header {
  margin-bottom: 0.5rem;
}

.comment-author {
  font-weight: 500;
  color: #000;
  margin-right: 0.5rem;
}

.comment-date {
  font-size: 0.875rem;
  color: #666;
}

.comment-text {
  font-size: 0.95rem;
  line-height: 1.4;
}

.comment-actions-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.actions-left {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.comment-menu {
  opacity: 0;
  transition: opacity 0.2s ease;
}

.comment-item:hover .comment-menu,
.reply-item:hover .comment-menu {
  opacity: 1;
}

.bi-three-dots-vertical {
  color: #666;
}

.btn-link:hover .bi-three-dots-vertical {
  color: var(--primary-color);
}

.replies {
  margin-left: 56px;
}

.reply-item {
  padding: 1rem 0;
}

.dropdown-item {
  font-size: 0.875rem;
  padding: 0.5rem 1rem;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}

.fa-ellipsis-v {
  color: #666;
  font-size: 1.2rem;
}

.btn-link {
  text-decoration: none;
}

.btn-link:hover {
  color: var(--primary-color) !important;
}

.edit-form,
.reply-form {
  margin-top: 1rem;
}

.replies-toggle {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0;
  color: var(--primary-color);
  font-size: 0.9rem;
}

.replies-toggle:hover {
  text-decoration: underline;
}

.bi-chevron-down {
  transition: transform 0.2s ease;
}

.bi-chevron-down.rotated {
  transform: rotate(180deg);
}

/* Make sure your existing replies class has these properties */
.replies {
  margin-left: 56px;
  transition: all 0.3s ease;
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
