import { useInfiniteQuery, useMutation } from '@tanstack/react-query';
import { getUserCookies, postCookie } from '../api/cookieRequest.js';

export const usePostCookie = () => {
  return useMutation({
    mutationFn: postCookie,
    onError: (error) => {
      console.log('Posting Cookie Error: ', error);
    },
  });
};

export const useGetUserCookies = (userId) => {
  return useInfiniteQuery({
    queryKey: ['cookies', userId],
    queryFn: ({ pageParam = 1 }) => {
      return getUserCookies({ userId, pageParam });
    },
    getNextPageParam: (lastPage) => {
      const { pageNumber, totalPages } = lastPage.data;

      if (pageNumber < totalPages) {
        return pageNumber + 1;
      } else {
        return undefined;
      }
    },
  });
};
