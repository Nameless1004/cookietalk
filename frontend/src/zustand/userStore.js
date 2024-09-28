import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const useUserStore = create(
  persist(
    (set) => {
      return {
        authenticatedUser: false,
        user: {
          userId: '',
          userNickname: '',
          accessToken: '',
        },
        signIn: ({ userId, userNickname, accessToken }) => {
          set(() => {
            return {
              authenticatedUser: true,
              user: {
                userId,
                userNickname,
                accessToken,
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
