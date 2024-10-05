import { useParams } from 'react-router-dom';
import ReactPlayer from 'react-player';
import { useEffect, useRef, useState } from 'react';

const Cookie = () => {
  const { cookieId } = useParams();
  const playerPositionRef = useRef(null);
  const [playerSize, setPlayerSize] = useState({ width: 0, height: 0 });

  useEffect(() => {
    if (playerPositionRef.current) {
      setPlayerSize({ width: playerPositionRef.current.offsetWidth, height: playerPositionRef.current.offsetHeight });
    }
  }, []);

  return (
    <div className='grid grid-cols-[2fr_1fr] gap-10 h-full'>
      <div
        id='cookieDetail'
        className='flex flex-col gap-3 bg-red-100'
      >
        <div
          ref={playerPositionRef}
          className='-bg--dark-gray-1 w-full aspect-video text-white'
        >
          <ReactPlayer
            url='https://www.youtube.com/watch?v=wojsSCBQJVc'
            width={playerSize.width}
            height={playerSize.height}
          />
        </div>
        <div>
          <h1 className='font-bold text-2xl'>쿠키 제목</h1>
          <p className='text-lg'>작성자 닉네임</p>
        </div>
        <div className='-bg--light-gray-1 p-3 h-[2000px]'>
          cookie description 작성한 상세 내용이 여기에 들어가요 작성한 상세 내용이 여기에 들어가요 작성한 상세 내용이
          여기에 들어가요 작성한 상세 내용이 여기에 들어가요 작성한 상세 내용이 여기에 들어가요 작성한 상세 내용이
          여기에 들어가요{' '}
        </div>
      </div>
      <div
        id='relativeCookies'
        className='bg-lime-100'
      ></div>
    </div>
  );
};

export default Cookie;
