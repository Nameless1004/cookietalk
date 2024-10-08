import { Play } from 'lucide-react';
const SeriesCard = ({ id, title }) => {
  // TODO: 해당 시리즈의 첫번째 쿠키 페이지로 이동하는 onClick 이벤트 추가
  return (
    <div className='w-[300px] h-[210px] rounded border flex-shrink-0 flex flex-col justify-center items-center hover:cursor-pointer group'>
      <div className='w-full h-[140px] flex justify-center items-center'>
        <Play
          size='50px'
          strokeWidth='1.5px'
          className='-text--dark-gray-1 group-hover:-text--dark-gray-0'
        />
      </div>
      <div className='w-full h-[70px] -bg--light-gray-1/50 flex items-center p-3 overflow-hidden group-hover:-bg--light-gray-1'>
        {title}
      </div>
    </div>
  );
};

export default SeriesCard;
