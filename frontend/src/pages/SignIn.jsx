import AuthForm from '../components/auth/AuthForm.jsx';
import { Link, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useSignIn } from '../query/userQuery.js';
import { authValidate } from '../utilities/validate.js';

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

    const { isValid, message } = authValidate({ input: formValue, mode: 'signin' });
    if (!isValid) {
      alert(message);
      return;
    }

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

export default SignIn;
