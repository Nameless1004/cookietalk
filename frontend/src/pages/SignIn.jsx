import AuthForm from '../components/auth/AuthForm.jsx';
import { Link, useNavigate } from 'react-router-dom';

const SignIn = () => {
  const [formValue, setFormValue] = useState({
    username: '',
    password: '',
  });

  const { mutate, isSuccess, error, isError, isPending } = useSignIn();

  const navigate = useNavigate();
  useEffect(() => {
    if (isSuccess) {
      navigate('/');
    }

    if (isError) {
      alert(`${error}: 로그인에 실패했습니다.`);
    }
  }, [isSuccess, isError]);

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: 유효성검사 추가
    mutate(formValue);
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

import { useEffect, useState } from 'react';
import { useSignIn } from '../query/userQuery.js';

export default SignIn;
