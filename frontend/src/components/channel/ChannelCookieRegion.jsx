import { Suspense } from 'react';

const ChannelCookieRegion = ({ userId, title, mode }) => {
  // TODO: mode에따라 userId의 시리즈/쿠키/최근본영상 fetch
  // TODO: userId null인 경우 처리
  // TODO: 데이터 가져오는 거 커스텀훅으로 만들기
  return (
    <div>
      <h2 className='font-bold text-xl'>{title}</h2>
      <div className='w-full h-[250px] bg-lime-50'>
        <Suspense fallback={null}>
          {/*TODO: title만 먼저 띄워두고 fetch 될때까지 다른 거 띄워지는지 확인*/}
          {/*  fetch한 데이터 map으로 렌더*/}
        </Suspense>
      </div>
    </div>
  );
};

export default ChannelCookieRegion;
