import { useLocation } from 'react-router-dom';
import useUserStore from '../zustand/userStore.js';
import { useGetChannelProfile } from '../query/channelQuery.js';
import TemporalError from '../components/TemporalError.jsx';
import { useEffect, useState } from 'react';
import ChannelCookieRegion from '../components/channel/ChannelCookieRegion.jsx';
import ChannelProfile from '../components/channel/ChannelProfile.jsx';
import UserSeries from '../components/channel/UserSeries.jsx';

const Channel = () => {
  const location = useLocation();
  // const { isMyChannel } = location.state || { isMyChannel: false }; //TODO: url 직접 입력해 들어오는 경우도 내 채널로 인지하도록 수정
  const { id } = useUserStore((state) => state.user);
  const [isMyChannel, setIsMyChannel] = useState(false);

  const { data, isPending: isProfilePending, isError, error, isSuccess } = useGetChannelProfile(id);

  useEffect(() => {
    if (isSuccess) {
      console.log(data); // TODO: 로그 제거

      if (data.userId === id) {
        setIsMyChannel(true);
      }
    }
  }, [isSuccess]);

  if (isError) {
    console.error(error);
    return (
      <TemporalError
        error={error}
        handleError={() => {
          location.reload();
        }}
        buttonMessage='새로고침'
      />
    );
  }

  return (
    <div>
      <ChannelProfile
        isMyChannel={isMyChannel}
        profile={data}
        isPending={isProfilePending}
      />
      <div className='flex flex-col gap-5 mt-5'>
        <ChannelCookieRegion title='토커의 시리즈'>
          <UserSeries userId={id} />
        </ChannelCookieRegion>
        <ChannelCookieRegion title='토커의 쿠키'></ChannelCookieRegion>
        {isMyChannel ? <ChannelCookieRegion title='최근 본 쿠키'></ChannelCookieRegion> : null}
      </div>
    </div>
  );
};

export default Channel;
