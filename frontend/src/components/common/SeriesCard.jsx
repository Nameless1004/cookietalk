const SeriesCard = ({ id, title }) => {
  // TODO: 해당 시리즈의 첫번째 쿠키 페이지로 이동하는 onClick 이벤트 추가
  return (
    <div className='w-[300px] h-[210px] bg-amber-200 border flex-shrink-0 flex justify-center items-center'>
      {title}
    </div>
  );
};

export default SeriesCard;
