// src/api/olympics.js

export const createOlympic = async (data) => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      console.log('Olympic created with data:', data);
      // Simulate a successful response
      resolve({ data: { success: true, id: 1, ...data } });
    }, 500); // Simulate network delay
  });
};


// import axios from './axios'; // Import your axios instance

// export const createOlympic = (data) => {
//   return axios.post('/api/olympics', data); // Use the actual backend endpoint
// };
