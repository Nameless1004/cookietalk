import { Link } from 'react-router-dom';
import useUserStore from '../../zustand/userStore.js';
import { useSignOut } from '../../query/userQuery.js';
import { useEffect } from 'react';
import SearchInput from '../inputs/SearchInput.jsx';

const Header = () => {
  const { authenticatedUser } = useUserStore((state) => state);
  const { mutate, isError, isPending } = useSignOut();

  useEffect(() => {
    if (isError) {
      alert('로그아웃 요청에 실패했습니다. 다시 한번 시도해주세요.');
      location.reload();
    }
  }, [isError]);

  return (
    <div className='grid grid-cols-3 items-center shadow shadow-gray-400/50 w-full h-[70px]'>
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
