import usersInstance from './instance/usersInstance.js';

export const getChannelProfile = async (userId) => {
  try {
    const response = await usersInstance.get(`/${userId}/channels/profile`);
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
