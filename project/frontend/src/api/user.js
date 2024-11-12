export const getUserProfile = async (userId) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        data: {
          id: userId,
          username: 'John Doe',
          email: 'johndoe@example.com',
          role: 'user',
        },
      });
    }, 500); // Simulate network delay
  });
};

export const updateUserProfile = async (userId, updatedDetails) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        data: { id: userId, ...updatedDetails },
      });
    }, 500); // Simulate network delay
  });
};
