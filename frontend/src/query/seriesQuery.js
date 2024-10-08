import { useQuery } from '@tanstack/react-query';
import { getUserSeries } from '../api/seriesRequest.js';

export const useGetUserSeries = (userId) => {
  const { data, error, isError } = useQuery({
    queryFn: () => getUserSeries(userId),
    suspense: true,
  });

  return { data, error, isError };
};
