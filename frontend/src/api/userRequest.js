import authInstance from './instance/authInstance.js';

export const postSignUp = async (userInput) => {
  try {
    const response = await authInstance.post('/signup', userInput);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const postSignIn = async (userInput) => {
  try {
    const response = await authInstance.post('/login', userInput);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const postSignOut = async (accessToken) => {
  try {
    const response = await authInstance.post(
      '/logout',
      {},
      {
        headers: {
          Authorization: { accessToken },
        },
      },
    );

    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
