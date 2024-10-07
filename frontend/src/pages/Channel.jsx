import { useLocation } from 'react-router-dom';
import useUserStore from '../zustand/userStore.js';
import { Github, Mail, Coffee, UserCog } from 'lucide-react';
import { useGetChannelProfile } from '../query/channelQuery.js';
import TemporalError from '../components/TemporalError.jsx';
import { useEffect, useState } from 'react';
import ChannelCookieRegion from '../components/channel/ChannelCookieRegion.jsx';

const Channel = () => {
  const location = useLocation();
  // const { isMyChannel } = location.state || { isMyChannel: false }; //TODO: url 직접 입력해 들어오는 경우도 내 채널로 인지하도록 수정
  const { id } = useUserStore((state) => state.user);
  const [isMyChannel, setIsMyChannel] = useState(false);

  const { data, isPending, isError, error, isSuccess } = useGetChannelProfile(id);

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
      <div className='grid grid-cols-[1fr_4fr] gap-10 items-center w-full h-[250px] -bg--light-gray-1 p-10'>
        <div
          id='profileImg'
          className='rounded-full w-[120px] h-[120px] overflow-hidden'
        >
          <img
            src={data?.profileImageUrl ? data.profileImageUrl : '/defaultProfile.png'}
            alt='프로필 이미지'
            className='w-full h-full object-cover'
          />
        </div>
        <div className='grid grid-rows-[1fr_3fr_1fr] h-full'>
          <div className='flex justify-start items-center gap-3'>
            <p className='font-bold'>{isPending ? '' : data.nickname}</p>
          </div>
          <div>{data?.description ?? '소개글이 없습니다.'}</div>
          <div className='flex items-center gap-3'>
            <button aria-label='User Github Link'>
              <Github strokeWidth='1.5px' />
            </button>
            <button aria-label='Copy User Mail'>
              <Mail strokeWidth='1.5px' />
            </button>
            <button aria-label='Support User'>
              <Coffee strokeWidth='1.5px' />
            </button>
            {isMyChannel ? (
              <button
                aria-label='Edit Channel Info'
                title='채널 정보 수정'
              >
                <UserCog strokeWidth='1.5px' />
              </button>
            ) : null}
          </div>
        </div>
      </div>
      <div className='flex flex-col gap-5 mt-5'>
        <ChannelCookieRegion
          userId={data?.userId}
          title='토커의 시리즈'
          mode='series'
        />
        <ChannelCookieRegion
          userId={data?.userId}
          title='토커의 쿠키'
          mode='cookies'
        />
        {isMyChannel ? (
          <ChannelCookieRegion
            userId={data?.userId}
            title='최근 본 쿠키'
            mode='recent'
          />
        ) : null}
      </div>
    </div>
  );
};

export default Channel;
