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
    const response = await authInstance.post('/signin', userInput);
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const postSignOut = async (accessToken) => {
  try {
    const response = await authInstance.post(
      '/signout',
      {},
      {
        headers: {
          Authorization: accessToken,
        },
      },
    );

    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
