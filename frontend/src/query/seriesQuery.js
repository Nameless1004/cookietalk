import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { getUserSeries, postSeries } from '../api/seriesRequest.js';

export const useGetUserSeries = (userId) => {
  const { data, error, isError, isSuccess } = useQuery({
    queryKey: ['userSeries', userId],
    queryFn: () => getUserSeries(userId),
    suspense: true,
  });

  return { data, error, isGetSeriesError: isError, isSuccess };
};

export const usePostSeries = (userId) => {
  const queryClient = useQueryClient();

  const { mutate, isPending, error, isError, isSuccess } = useMutation({
    mutationFn: postSeries,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['postSeries', userId] });
    },
  });

  return { mutate, isPending, error, isError, isSuccess };
};
