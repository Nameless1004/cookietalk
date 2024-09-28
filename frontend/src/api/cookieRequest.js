import { authCookieInstance } from './instance/cookieInstance.js';

export const postCookie = async (input) => {
  const formData = new FormData();
  formData.append(
    'create',
    new Blob([JSON.stringify({ title: input.title, description: input.description, categoryId: 1, seriesId: null })], {
      type: 'application/json',
    }),
  );

  formData.append('video', input.video);
  formData.append('thumbnail', input.thumbnail);
  formData.append('attachment', input.attachment);

  try {
    const response = await authCookieInstance.post('', formData, {
      headers: {
        'Content-Type': 'multipart/form-data', // JSON 데이터 타입 명시
      },
    });

    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
