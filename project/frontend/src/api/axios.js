// // src/api/axios.js
// import axios from 'axios';

// const instance = axios.create({
//   baseURL: '/api', // Matches the proxy setup in vite.config.js
//   withCredentials: true,
// });

// export default instance;
import axios from 'axios';

const instance = axios.create({
  baseURL: '/api', // Proxy will forward to the backend
  timeout: 1000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Mock interceptor for development purposes
instance.interceptors.request.use((config) => {
  console.log('Mock API Request:', config);
  return config;
});

export default instance;
