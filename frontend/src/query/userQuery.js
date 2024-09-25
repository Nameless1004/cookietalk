import { useMutation, useQueryClient } from '@tanstack/react-query';
import { postSignIn, postSignUp } from '../api/userRequest.js';
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
  const queryClient = useQueryClient();
  const { mutate, isPending, error, isError, isSuccess } = useMutation({
    mutationFn: postSignIn,
    onSuccess: (data) => {
      const { username, nickname, accessToken } = data[0];
      const { signIn } = useUserStore((state) => state);
      signIn({ username, nickname, accessToken });
    },
    onError: (error) => {
      console.log('Sign In Error: ', error);
    },
  });

  return { mutate, isPending, error, isError, isSuccess };
};
