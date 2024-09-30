import { useParams } from 'react-router-dom';

const Cookie = () => {
  const { cookieId } = useParams();
  return <div>업로드된 쿠키 아이디: {cookieId}</div>;
};

export default Cookie;
