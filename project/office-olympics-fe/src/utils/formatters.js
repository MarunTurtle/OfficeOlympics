/**
 * Format a date into a readable string
 * @param {string | Date} date - The date to format
 * @returns {string} - Formatted date string (e.g., "Nov 16, 2024")
 */
export const formatDate = (date) => {
  const options = { year: 'numeric', month: 'short', day: 'numeric' };
  return new Date(date).toLocaleDateString(undefined, options);
};

/**
 * Capitalize the first letter of a string
 * @param {string} str - The string to capitalize
 * @returns {string} - String with the first letter capitalized
 */
export const capitalize = (str) => {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1);
};

/**
 * Format a number with commas
 * @param {number} num - The number to format
 * @returns {string} - Formatted number with commas (e.g., "1,000")
 */
export const formatNumber = (num) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};
