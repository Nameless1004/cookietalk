import authInstance from './instance/authInstance.js';
import useUserStore from '../zustand/userStore.js';

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

export const postSignOut = async (accessToken) => {
  try {
    const response = await authInstance.post('/api/users/logout', {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const postCookie = async (input) => {
  const storedData = localStorage.getItem('user');
  const parsedData = JSON.parse(storedData);
  const accessToken = parsedData?.state?.user?.accessToken;

  const formData = new FormData();
  formData.append(
    'create',
    new Blob([JSON.stringify({ title: input.title, description: input.description, categoryId: 0, seriesId: null })], {
      type: 'application/json',
    }),
  );

  formData.append('video', input.video);
  formData.append('thumbnail', input.thumbnail);
  formData.append('attachment', input.attachment);

  try {
    const response = await authInstance.post('/api/cookies', formData, {
      headers: {
        Authorization: `Bearer ${accessToken}`, // Authorization 헤더 추가
        'Content-Type': 'multipart/form-data', // JSON 데이터 타입 명시
      },
    });

    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
