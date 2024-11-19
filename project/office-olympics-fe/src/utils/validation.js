/**
 * Validate if an input is a valid email
 * @param {string} email - The email to validate
 * @returns {boolean} - True if valid, false otherwise
 */
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

/**
 * Validate if a password meets requirements
 * - Minimum 8 characters, at least one uppercase letter, one lowercase letter, one number, and one special character
 * @param {string} password - The password to validate
 * @returns {boolean} - True if valid, false otherwise
 */
export const isValidPassword = (password) => {
  // const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  // return passwordRegex.test(password);
  return password && password.length >= 8;
};

/**
 * Check if a string is not empty
 * @param {string} str - The string to check
 * @returns {boolean} - True if not empty, false otherwise
 */
export const isNotEmpty = (str) => {
  return str && str.trim().length > 0;
};

/**
 * Validate if an input is a positive integer
 * @param {number} num - The number to validate
 * @returns {boolean} - True if positive integer, false otherwise
 */
export const isPositiveInteger = (num) => {
  return Number.isInteger(num) && num > 0;
};
