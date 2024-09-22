import { useNavigate } from 'react-router-dom';

const Main = () => {
  const navigate = useNavigate();
  const moveToPostCookie = () => {
    navigate('/postCookie');
  };

  return (
    <div>
      <button
        type='button'
        onClick={moveToPostCookie}
      >
        쿠키 올리기
      </button>
    </div>
  );
};

export default Main;
