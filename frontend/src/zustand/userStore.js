import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const useUserStore = create(
  persist(
    (set) => {
      return {
        authenticatedUser: false,
        user: {
          id: '',
          userId: '',
          nickname: '',
          accessToken: '',
          refreshToken: '',
        },
        signIn: ({ id, userId, nickname, accessToken, refreshToken }) => {
          set(() => {
            return {
              authenticatedUser: true,
              user: {
                id,
                userId,
                nickname,
                accessToken,
                refreshToken,
              },
            };
          });
        },
        signOut: () => {
          set(() => {
            return {
              authenticatedUser: false,
              user: {
                userId: '',
                userNickname: '',
                accessToken: '',
              },
            };
          });
        },
      };
    },
    {
      name: 'user',
      getStorage: () => localStorage,
    },
  ),
);

export default useUserStore;
