import authenticatedUsersInstance from './instance/usersInstance.js';

export const getUserSeries = async (userId) => {
  try {
    const response = await authenticatedUsersInstance.get(`/${userId}/series`);
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
