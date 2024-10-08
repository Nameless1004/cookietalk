import authenticatedUsersInstance from './instance/usersInstance.js';

export const getChannelProfile = async (userId) => {
  try {
    const response = await authenticatedUsersInstance.get(`/${userId}/channels/profile`);
    return response.data.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const patchChannelProfile = async (updatedProfile) => {
  const { userId } = updatedProfile;

  console.log('patchChannelProfile ~ updatedProfile: ', updatedProfile);

  const formData = new FormData();

  formData.append('nickname', updatedProfile.nickname ?? '');
  formData.append('description', updatedProfile.description ?? '');
  formData.append('githubUrl', updatedProfile.githubUrl ?? '');
  formData.append('blogUrl', updatedProfile.blogUrl ?? '');
  formData.append('businessEmail', updatedProfile.businessEmail ?? '');
  formData.append('profile', updatedProfile.profileImg);

  try {
    const response = await authenticatedUsersInstance.patch(`/${userId}/channels/profile`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
