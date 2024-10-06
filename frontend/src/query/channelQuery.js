import { useQuery } from '@tanstack/react-query';
import { getChannelProfile } from '../api/channelRequest.js';

export const useGetChannelProfile = (userId) => {
  const { data, isPending, error, isError, isSuccess } = useQuery({
    queryKey: ['channelProfile', userId],
    queryFn: () => getChannelProfile(userId),
  });

  return { data, isPending, error, isError, isSuccess };
};
