import { useMutation } from '@tanstack/react-query';
import { postSignIn, postSignOut, postSignUp } from '../api/userRequest.js';
import useUserStore from '../zustand/userStore.js';

export const useSignUp = () => {
  const { mutate, isPending, error, isError, isSuccess } = useMutation({
    mutationFn: postSignUp,
    onError: (error) => {
      console.log('Sign Up Error: ', error);
    },
  });

  return { mutate, isPending, error, isError, isSuccess };
};

export const useSignIn = () => {
  const { signIn } = useUserStore((state) => state);
  const { mutate, isPending, error, isError, isSuccess } = useMutation({
    mutationFn: postSignIn,
    onSuccess: (data) => {
      const { id, userId, userNickname, accessToken, refreshToken } = data;
      signIn({ id, userId, nickname: userNickname, accessToken, refreshToken });
    },
    onError: (error) => {
      console.log('Sign In Error: ', error);
    },
  });

  return { mutate, isPending, error, isError, isSuccess };
};

export const useSignOut = () => {
  const { signOut, user } = useUserStore((state) => state);
  const accessToken = user.accessToken;
  const { mutate, isPending, error, isError } = useMutation({
    mutationFn: () => {
      postSignOut(accessToken);
    },
    onSuccess: () => {
      signOut();
    },
    onError: (error) => {
      console.log('Sign Out Error: ', error);
    },
  });

  return { mutate, isPending, error, isError };
};
