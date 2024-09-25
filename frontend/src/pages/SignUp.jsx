import { useState } from 'react';
import FormInput from '../components/inputs/FormInput.jsx';
import Button from '../components/inputs/Button.jsx';
import { useSignUp } from '../query/userQuery.js';
import { Navigate } from 'react-router-dom';

const SignUp = () => {
  const [formValue, setFormValue] = useState({
    username: '',
    password: '',
    email: '',
    nickname: '',
  });

  const { mutate, isSuccess, error, isError, isPending } = useSignUp();
  if (isSuccess) {
    alert('회원가입이 완료되었습니다.');
    <Navigate to='signIn' />;
  }

  if (isError) {
    alert(`${error}: 회원가입이 실패했습니다. 다시 한번 시도해주세요.`);
    window.location.reload();
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    mutate(formValue);
  };

  return (
    <div className='flex flex-col gap-5 justify-center items-center mt-10'>
      <h1 className='font-bold text-xl'>회원가입</h1>
      <form onSubmit={handleSubmit}>
        <FormInput
          label='아이디'
          name='username'
          formValue={formValue}
          setFormValue={setFormValue}
          placeholder='아이디를 입력해주세요.'
        />
        <FormInput
          label='비밀번호'
          name='password'
          type='password'
          formValue={formValue}
          setFormValue={setFormValue}
          placeholder='비밀번호를 입력해주세요.'
        />
        <FormInput
          label='이메일'
          name='email'
          formValue={formValue}
          setFormValue={setFormValue}
          type='email'
          placeholder='이메일을 입력해주세요.'
        />
        <FormInput
          label='닉네임'
          name='nickname'
          formValue={formValue}
          setFormValue={setFormValue}
          placeholder='닉네임을 입력해주세요.'
        />
        <Button
          type='submit'
          styleType='primary'
          className='w-full'
        >
          등록하기
        </Button>
      </form>
    </div>
  );
};

export default SignUp;
