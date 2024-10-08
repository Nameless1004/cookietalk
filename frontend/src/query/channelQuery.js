import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { getChannelProfile, patchChannelProfile } from '../api/channelRequest.js';
import useUserStore from '../zustand/userStore.js';

export const useGetChannelProfile = (userId) => {
  const { data, isPending, error, isError, isSuccess } = useQuery({
    queryKey: ['channelProfile', userId],
    queryFn: () => getChannelProfile(userId),
  });

  return { data, isPending, error, isError, isSuccess };
};

export const usePatchChannelProfile = () => {
  const queryClient = useQueryClient();
  const { id: authId } = useUserStore((state) => state.user);

  const { mutate, isPending, error, isError, isSuccess } = useMutation({
    mutationFn: patchChannelProfile,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['channelProfile', authId] });
    },
    onError: (error) => {
      console.log('Profile Update Error: ', error);
    },
  });

  return { mutate, isPending, error, isError, isSuccess };
};
