import { useParams } from 'react-router-dom';

const Cookie = () => {
  const { cookieId } = useParams();
  return (
    <div className='grid grid-cols-[2fr_1fr] gap-5 h-full'>
      <div
        id='cookieDetail'
        className='flex flex-col gap-3 bg-red-100 h-full'
      >
        <div className='-bg--dark-gray-1 w-full h-[450px] text-white'>비디오 플레이어 자리</div>
        <div>
          <h1 className='font-bold text-2xl'>쿠키 제목</h1>
          <p className='text-lg'>작성자 닉네임</p>
        </div>
        <div className='-bg--light-gray-1 p-3 h-[250px]'>
          cookie description 작성한 상세 내용이 여기에 들어가요 작성한 상세 내용이 여기에 들어가요 작성한 상세 내용이
          여기에 들어가요 작성한 상세 내용이 여기에 들어가요 작성한 상세 내용이 여기에 들어가요 작성한 상세 내용이
          여기에 들어가요{' '}
        </div>
      </div>
      <div
        id='relativeCookies'
        className='bg-lime-100 h-full'
      ></div>
    </div>
  );
};

export default Cookie;
