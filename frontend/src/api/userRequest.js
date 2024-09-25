import authInstance from './instance/authInstance.js';

export const postSignUp = async (userInput) => {
  try {
    const response = await authInstance.post('/signup', userInput);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
