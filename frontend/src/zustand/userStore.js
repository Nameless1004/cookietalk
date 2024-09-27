import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const useUserStore = create(
  persist((set) => {
    return {
      authenticatedUser: false,
      user: {
        userId: '',
        userNickname: '',
      },
      signIn: ({ userId, userNickname }) => {
        set(() => {
          return {
            authenticatedUser: true,
            user: {
              userId,
              userNickname,
            },
          };
        });
      },
      signOut: () => {
        return {
          authenticatedUser: false,
          user: {
            userId: '',
            userNickname: '',
          },
        };
      },
    };
  }),
  {
    name: 'userStore',
    getStorage: () => sessionStorage,
  },
);

export default useUserStore;
