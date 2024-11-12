// src/api/challenges.js

// import axios from 'axios';

// Get challenge details by ID
// export const getChallengeDetail = (id) => {
//   return axios.get(`/api/challenges/${id}`);
// };

// Submit a challenge record
// export const submitChallengeRecord = (id, record) => {
//   return axios.post(`/api/challenges/${id}/submit`, { record });
// };

// Get the leaderboard for a challenge
// export const getLeaderboard = (id) => {
//   return axios.get(`/api/challenges/${id}/leaderboard`);
// };
export const getChallengeDetail = async (id) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        data: {
          id,
          name: 'Wall Sit Challenge',
          description: 'Hold a wall sit position for as long as you can.',
        },
      });
    }, 500); // Simulate network delay
  });
};

export const submitChallengeRecord = async (id, record) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      console.log(`Record submitted: Challenge ID ${id}, Record ${record}`);
      resolve({ data: { success: true } });
    }, 500); // Simulate network delay
  });
};

export const getLeaderboard = async (id) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        data: [
          { userId: 1, username: 'Alice', record: '2 min' },
          { userId: 2, username: 'Bob', record: '1.5 min' },
          { userId: 3, username: 'Charlie', record: '1 min' },
        ],
      });
    }, 500); // Simulate network delay
  });
};

// Add the missing getChallenges function
export const getChallenges = async () => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        data: [
          {
            id: 1,
            name: 'Wall Sit Challenge',
            description: 'Hold a wall sit position for as long as you can.',
          },
          {
            id: 2,
            name: 'Speed Typing Challenge',
            description: 'Type as fast as you can within a minute.',
          },
          {
            id: 3,
            name: 'Paper Airplane Throwing',
            description: 'Throw a paper airplane as far as possible.',
          },
        ],
      });
    }, 500); // Simulate network delay
  });
};
