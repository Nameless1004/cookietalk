import { Coffee, Github, Mail, UserCog } from 'lucide-react';
import { useEffect, useState } from 'react';
import ImageInput from '../inputs/ImageInput.jsx';
import FormInput from '../inputs/FormInput.jsx';
import Button from '../inputs/Button.jsx';
import { usePatchChannelProfile } from '../../query/channelQuery.js';
import { channelProfileValidate } from '../../utilities/validate.js';

const ChannelProfile = ({ isMyChannel, profile, isPending }) => {
  const [editMode, setEditMode] = useState(false);
  const [profileInput, setProfileInput] = useState({
    channelId: null,
    userId: null,
    nickname: null,
    description: null,
    githubUrl: null,
    blogUrl: null,
    businessEmail: null,
    profileImg: null,
  });

  const setProfileImg = (value) => {
    setProfileInput({ ...profileInput, profileImg: value });
  };

  useEffect(() => {
    setProfileInput({ ...profile, profileImg: null });
  }, [isPending]);

  const {
    mutate: mutateProfile,
    isPending: mutateProfileIsPending,
    error,
    isError,
    isSuccess,
  } = usePatchChannelProfile();

  useEffect(() => {
    if (isSuccess) {
      alert('프로필 수정 성공!');
    }

    if (isError) {
      alert(`프로필 업데이트 에러: ${error}`);
    }
  }, [isSuccess, isError]);

  const handleToggleEditMode = () => {
    setEditMode(!editMode);
  };

  const handleProfileSubmit = (e) => {
    e.preventDefault();

    const { isValid, message } = channelProfileValidate(profileInput);
    if (!isValid) {
      alert(message);
      return;
    }

    mutateProfile(profileInput);
    setEditMode(false);
  };

  return editMode ? (
    <form
      onSubmit={handleProfileSubmit}
      className='grid grid-cols-[1fr_4fr] gap-10 items-center w-full h-[250px] -bg--light-gray-1 p-10'
    >
      <ImageInput
        value={profileInput.profileImg}
        setValue={setProfileImg}
        originUrl={profile.profileImageUrl}
        buttonLabel='이미지 선택'
      />
      <div className='flex flex-col gap-3 h-full'>
        <FormInput
          type='text'
          name='nickname'
          formValue={profileInput}
          setFormValue={setProfileInput}
          label='닉네임 *'
          placeholder='닉네임을 입력해주세요'
        />
        <FormInput
          name='blogUrl'
          formValue={profileInput}
          setFormValue={setProfileInput}
          label='블로그 주소 *'
          placeholder='블로그 주소를 입력해주세요'
        />
        <FormInput
          name='businessEmail'
          formValue={profileInput}
          setFormValue={setProfileInput}
          label='이메일 주소 *'
          placeholder='이메일 주소를 입력해주세요'
        />
        <FormInput
          name='githubUrl'
          formValue={profileInput}
          setFormValue={setProfileInput}
          label='깃허브 주소 *'
          placeholder='깃허브 주소를 입력해주세요'
        />
        <FormInput
          name='description'
          formValue={profileInput}
          setFormValue={setProfileInput}
          label='채널 소개 *'
          placeholder='채널 소개를 작성해주세요'
        />
        <div className='flex'>
          <Button
            type='submit'
            styleType='primary'
            sizeType='full'
          >
            수정하기
          </Button>
          <Button
            type='button'
            styleType='sub'
            className='bg-white'
            sizeType='full'
            onClick={handleToggleEditMode}
          >
            닫기
          </Button>
        </div>
      </div>
    </form>
  ) : (
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
              onClick={handleToggleEditMode}
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
