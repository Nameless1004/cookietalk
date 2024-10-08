import { useGetUserSeries } from '../../query/seriesQuery.js';
import SeriesCard from '../common/SeriesCard.jsx';
import { useEffect } from 'react';

const UserSeries = ({ userId }) => {
  const { data, isError, error } = useGetUserSeries(userId);

  useEffect(() => {
    if (isError) {
      console.error('GET User Series Error: ', error);
    }
  }, [isError]);

  if (!data || data.length === 0)
    return <div className='w-full h-full flex justify-center items-center'>토커의 시리즈가 없습니다.</div>;

  return (
    <>
      {data?.map((series) => {
        return (
          <SeriesCard
            key={`${series.id}`}
            id={series.id}
            title={series.title}
          />
        );
      })}
    </>
  );
};

export default UserSeries;
