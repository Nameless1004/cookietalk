import FormInput from '../inputs/FormInput.jsx';
import Button from '../inputs/Button.jsx';

const AuthForm = ({ onSubmit, mode, formValue, setFormValue }) => {
  return (
    <div className='flex flex-col gap-5 justify-center items-center mt-10'>
      <h1 className='font-bold text-xl'>{mode === 'signup' ? '회원가입' : '로그인'}</h1>
      <form
        noValidate
        className='flex flex-col gap-2'
        onSubmit={onSubmit}
      >
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
        {mode === 'signup' ? (
          <FormInput
            label='이메일'
            name='email'
            formValue={formValue}
            setFormValue={setFormValue}
            type='email'
            placeholder='이메일을 입력해주세요.'
          />
        ) : null}
        {mode === 'signup' ? (
          <FormInput
            label='닉네임'
            name='nickname'
            formValue={formValue}
            setFormValue={setFormValue}
            placeholder='닉네임을 입력해주세요.'
          />
        ) : null}
        <Button
          type='submit'
          styleType='primary'
          sizeType='full'
          className='mt-1'
        >
          {mode === 'signup' ? '등록하기' : '로그인하기'}
        </Button>
      </form>
    </div>
  );
};

export default AuthForm;
