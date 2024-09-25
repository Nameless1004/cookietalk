import { useMutation } from '@tanstack/react-query';
import { postSignUp } from '../api/userRequest.js';

export const useSignUp = () => {
  const { mutate, isPending, error, isError, isSuccess } = useMutation({
    mutationFn: postSignUp,
    onError: (error) => {
      console.log('Sign Up Error: ', error);
    },
  });

  return { mutate, isPending, error, isError, isSuccess };
};
