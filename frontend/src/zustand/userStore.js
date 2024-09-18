import { create } from 'zustand';

const useUserStore = create((set) => {
  return {
    authenticatedUser: true,
    signIn: () => {
      set(() => {
        return {
          authenticatedUser: true,
          // TODO: 로그인 API에서 받아올 수 있는 정보들 세팅
        };
      });
    },
    signOut: () => {
      return {
        authenticatedUser: false,
        // TODO: 로그인시 세팅한 정보들 초기값으로 돌려두기
      };
    },
  };
});

export default useUserStore;
