import { useLocation } from 'react-router-dom';
import useUserStore from '../zustand/userStore.js';
import { Github, Mail, Coffee, UserCog } from 'lucide-react';
import { useGetChannelProfile } from '../query/channelQuery.js';

const Channel = () => {
  const location = useLocation();
  const { isMyChannel } = location.state;
  const { id } = useUserStore((state) => state.user);

  const { data, isPending, isError, error, isSuccess } = useGetChannelProfile(id);

  if (isSuccess) {
    alert('성공');
    console.log(data);
  }

  return (
    <div>
      <div className='grid grid-cols-[1fr_4fr] gap-10 items-center w-full h-[250px] -bg--light-gray-1 p-10'>
        <div
          id='profileImg'
          className='rounded-full bg-red-300 w-[120px] h-[120px] overflow-hidden'
        >
          <img
            src='/defaultProfile.png'
            alt='프로필 이미지'
            className='w-full h-full object-cover'
          />
        </div>
        <div className='grid grid-rows-[1fr_3fr_1fr] h-full'>
          <div className='flex justify-start items-center gap-3'>
            {/*<p className='font-bold'>{isPending ? '' : data.nickname}</p>*/}
            <p>구독자수 nn명</p>
          </div>
          <div>소개글이 없습니다.</div>
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
              <button aria-label='Edit Channel Info'>
                <UserCog strokeWidth='1.5px' />
              </button>
            ) : null}
          </div>
        </div>
      </div>
      <div>
        <h2></h2>
      </div>
    </div>
  );
};

export default Channel;
