import { useState } from 'react';
import { useSignUp } from '../query/userQuery.js';
import { Link, Navigate } from 'react-router-dom';
import AuthForm from '../components/auth/AuthForm.jsx';

const SignUp = () => {
  const [formValue, setFormValue] = useState({
    username: '',
    password: '',
    email: '',
    nickname: '',
  });

  const { mutate, isSuccess, error, isError, isPending } = useSignUp();
  if (isSuccess) {
    alert('회원가입이 완료되었습니다. 로그인을 완료해주세요!');
    return <Navigate to='signIn' />;
  }

  if (isError) {
    alert(`${error}: 회원가입이 실패했습니다. 다시 한번 시도해주세요.`);
    window.location.reload();
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('회원가입 전달 데이터', formValue);
    mutate(formValue);
  };

  return (
    <div className='flex flex-col items-center gap-3'>
      <AuthForm
        mode='signup'
        onSubmit={handleSubmit}
        formValue={formValue}
        setFormValue={setFormValue}
      />
      <p>
        이미 계정이 있으신가요?{' '}
        <Link
          className='-text--primary-orange'
          to='/signIn'
        >
          로그인
        </Link>
      </p>
    </div>
  );
};

export default SignUp;
