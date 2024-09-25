import AuthForm from '../components/auth/AuthForm.jsx';
import { Link } from 'react-router-dom';

const SignIn = () => {
  const [formValue, setFormValue] = useState({
    username: '',
    password: '',
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(formValue);
    alert('로그인에 성공했습니다');
  };

  return (
    <div className='flex flex-col items-center gap-3'>
      <AuthForm
        mode='signin'
        onSubmit={handleSubmit}
        formValue={formValue}
        setFormValue={setFormValue}
      />
      <p>
        아직 계정이 없으신가요?{' '}
        <Link
          className='-text--primary-orange'
          to='/signUp'
        >
          회원가입
        </Link>
      </p>
    </div>
  );
};

import { useState } from 'react';

export default SignIn;
