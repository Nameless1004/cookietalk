import { useEffect, useState } from 'react';
import { useSignUp } from '../query/userQuery.js';
import { Link, useNavigate } from 'react-router-dom';
import AuthForm from '../components/auth/AuthForm.jsx';
import { authValidate } from '../utilities/validate.js';

const SignUp = () => {
  const [formValue, setFormValue] = useState({
    username: '',
    password: '',
    email: '',
    nickname: '',
  });

  const navigate = useNavigate();

  const { mutate, isSuccess, error, isError, isPending } = useSignUp();

  useEffect(() => {
    if (isSuccess) {
      alert('회원가입에 성공했습니다. 로그인을 완료해주세요!');
      navigate('/signin');
    }

    if (isError) {
      alert(`${error}: 회원가입에 실패했습니다. 다시 한번 시도해주세요.`);
    }
  }, [isSuccess, isError]);

  const handleSubmit = (e) => {
    e.preventDefault();

    const { isValid, message } = authValidate({ input: formValue, mode: 'signup' });
    if (!isValid) {
      alert(message);
      return;
    }

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
