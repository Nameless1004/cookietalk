import { create } from 'zustand';
import { persist } from 'zustand/middleware/persist';

const useUserStore = create(
  persist((set) => {
    return {
      authenticatedUser: false,
      user: {
        username: '',
        nickname: '',
      },
      signIn: ({ nickname, username, accessToken }) => {
        set(() => {
          return {
            authenticatedUser: true,
            // TODO: 로그인 API에서 받아올 수 있는 정보들 세팅
            user: {
              username,
              nickname,
            },
          };
        });
      },
      signOut: () => {
        return {
          authenticatedUser: false,
          // TODO: 로그인시 세팅한 정보들 초기값으로 돌려두기
          user: {
            username: '',
            nickname: '',
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
