import authenticatedUsersInstance from './instance/usersInstance.js';
import { authenticatedSeriesInstance } from './instance/seriesInstance.js';

export const getUserSeries = async (userId) => {
  try {
    const response = await authenticatedUsersInstance.get(`/${userId}/series`);
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const postSeries = async (seriesTitle) => {
  try {
    const response = await authenticatedSeriesInstance.post('', { title: seriesTitle });
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
