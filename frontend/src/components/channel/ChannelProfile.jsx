import { Coffee, Github, Mail, UserCog } from 'lucide-react';

const ChannelProfile = ({ isMyChannel, profile, isPending }) => {
  return (
    <div className='grid grid-cols-[1fr_4fr] gap-10 items-center w-full h-[250px] -bg--light-gray-1 p-10'>
      <div
        id='profileImg'
        className='rounded-full w-[120px] h-[120px] overflow-hidden'
      >
        <img
          src={profile?.profileImageUrl ? profile.profileImageUrl : '/defaultProfile.png'}
          alt='프로필 이미지'
          className='w-full h-full object-cover'
        />
      </div>
      <div className='grid grid-rows-[1fr_3fr_1fr] h-full'>
        <div className='flex justify-start items-center gap-3'>
          <p className='font-bold'>{isPending ? '' : profile.nickname}</p>
        </div>
        <div>{profile?.description ?? '소개글이 없습니다.'}</div>
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
  );
};

export default ChannelProfile;
