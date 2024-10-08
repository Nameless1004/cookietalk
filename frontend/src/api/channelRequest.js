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
  formData.append(
    'update',
    new Blob([
      JSON.stringify({
        channelId: updatedProfile.channelId,
        userId: updatedProfile.userId,
        nickname: updatedProfile.nickname,
        description: updatedProfile.description,
        githubUrl: updatedProfile.githubUrl,
        blogUrl: updatedProfile.blogUrl,
        businessEmail: updatedProfile.businessEmail,
      }),
    ]),
  );

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
