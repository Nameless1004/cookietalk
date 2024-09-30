import { useMutation } from '@tanstack/react-query';
import { postCookie } from '../api/cookieRequest.js';

export const usePostCookie = () => {
  return useMutation({
    mutationFn: postCookie,
    onError: (error) => {
      console.log('Posting Cookie Error: ', error);
    },
  });
};
