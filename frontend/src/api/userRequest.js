import authInstance from './instance/authInstance.js';

export const postSignUp = async (userInput) => {
  try {
    const response = await authInstance.post('/api/users/signup', userInput);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const postSignIn = async (userInput) => {
  try {
    const response = await authInstance.post('/api/users/login', userInput);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
