import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // Include cookies with requests
});

export const getImageUrl = (path) =>
  `${import.meta.env.VITE_IMAGE_BASE_URL}/${path}`;

export default api;
