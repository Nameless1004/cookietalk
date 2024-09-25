import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <div className='flex flex-row justify-around items-center shadow shadow-gray-400/50 w-full h-[50px]'>
      <div>로고</div>
      <div className='flex gap-5'>
        <Link to='/signIn'>로그인</Link>
        <Link to='/signUp'>회원가입</Link>
      </div>
    </div>
  );
};

export default Header;
