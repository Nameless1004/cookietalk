import { useNavigate } from 'react-router-dom';
import useUserStore from '../zustand/userStore.js';

const useNavigateChannel = () => {
  const navigate = useNavigate();
  const { id, nickname } = useUserStore((state) => state.user);

  const navigateToMyChannel = () => {
    navigate(`/channel/${nickname}`, { state: { id: id, isMyChannel: true } });
  };

  const navigateToUserChannel = ({ userId, nickname }) => {
    const isMyChannel = id === userId;
    navigate(`/channel/${nickname}`, { state: { id: userId, isMyChannel } });
  };

  return { navigateToMyChannel, navigateToUserChannel };
};

export default useNavigateChannel;
