import { Link } from 'react-router-dom';
import useUserStore from '../../zustand/userStore.js';
import { useSignOut } from '../../query/userQuery.js';
import { useEffect } from 'react';
import SearchInput from '../inputs/SearchInput.jsx';
import useNavigateChannel from '../../customHook/useNavigateChannel.js';

const Header = () => {
  const { authenticatedUser } = useUserStore((state) => state);
  const { mutate, isError, isPending } = useSignOut();

  useEffect(() => {
    if (isError) {
      alert('로그아웃 요청에 실패했습니다. 다시 한번 시도해주세요.');
      location.reload();
    }
  }, [isError]);

  const { navigateToMyChannel } = useNavigateChannel();

  return (
    <div className='bg-white grid grid-cols-3 items-center shadow shadow-gray-400/50 w-full h-[70px]'>
      <div></div>
      <div className='flex justify-center'>
        <Link to='/'>
          <img
            width='200px'
            src='/logo.png'
            alt='logo'
          />
        </Link>
      </div>
      <div className='flex justify-end gap-5 mr-[50px]'>
        <SearchInput />
        {authenticatedUser ? (
          <>
            <Link to='/postCookie'>쿠키 올리기</Link>
            <button
              disabled={isPending}
              onClick={mutate}
            >
              {isPending ? '처리중' : '로그아웃'}
            </button>
            <button onClick={navigateToMyChannel}>내 채널</button>
          </>
        ) : (
          <>
            <Link to='/signIn'>로그인</Link>
            <Link to='/signUp'>회원가입</Link>
          </>
        )}
      </div>
    </div>
  );
};

export default Header;
