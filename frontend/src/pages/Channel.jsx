import { useLocation } from 'react-router-dom';
import { useGetChannelProfile } from '../query/channelQuery.js';
import TemporalError from '../components/TemporalError.jsx';
import ChannelCookieRegion from '../components/channel/ChannelCookieRegion.jsx';
import ChannelProfile from '../components/channel/ChannelProfile.jsx';
import UserSeries from '../components/channel/UserSeries.jsx';
import UserCookies from '../components/channel/UserCookies.jsx';

const Channel = () => {
  const location = useLocation();
  const { id, isMyChannel } = location.state;
  const { data, isPending: isProfilePending, isError, error, isSuccess } = useGetChannelProfile(id);

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
        <ChannelCookieRegion title='토커의 쿠키'>
          <UserCookies userId={id} />
        </ChannelCookieRegion>
        {isMyChannel ? <ChannelCookieRegion title='최근 본 쿠키'></ChannelCookieRegion> : null}
      </div>
    </div>
  );
};

export default Channel;
