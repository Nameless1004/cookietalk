import { Link } from 'react-router-dom';
import useUserStore from '../../zustand/userStore.js';

const Header = () => {
  const { authenticatedUser, signOut } = useUserStore((state) => state);

  return (
    <div className='flex flex-row justify-around items-center shadow shadow-gray-400/50 w-full h-[50px]'>
      <Link to='/'>로고</Link>

      <div className='flex gap-5'>
        {authenticatedUser ? (
          <>
            <Link to='/postCookie'>쿠키 올리기</Link>
            <button onClick={signOut}>로그아웃</button>
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
