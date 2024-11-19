import './assets/styles/main.css';

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import { useAuthStore } from '@/stores/auth';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);

// Load the user from localStorage on app start
const authStore = useAuthStore();
authStore.loadUser();

app.mount('#app');
