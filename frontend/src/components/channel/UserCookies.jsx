import { useGetUserCookies } from '../../query/cookieQuery.js';
import { Fragment, useEffect, useRef } from 'react';
import DataLoading from '../common/DataLoading.jsx';
import TemporalError from '../TemporalError.jsx';
import CookieCard from '../common/CookieCard.jsx';

const UserCookies = ({ userId }) => {
  const { data, error, fetchNextPage, hasNextPage, isFetchingNextPage, status } = useGetUserCookies(userId);

  const observerCard = useRef(null);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNextPage) {
          fetchNextPage();
        }
      },
      { threshold: 0.5 },
    );

    if (observerCard.current) {
      observer.observe(observerCard.current);
    }

    return () => {
      if (observerCard.current) {
        observer.unobserve(observerCard.current);
      }
    };
  }, [hasNextPage]);

  console.log('data: ', data);

  if (!data || !data.pages || data.pages.length === 0 || !data.pages[0])
    return <div className='w-full h-full flex justify-center items-center'>토커의 쿠키가 없습니다.</div>;

  if (status === 'loading') {
    return <DataLoading />;
  }

  if (status === 'error') {
    console.log('UserCookies Error: ', error);
    return <TemporalError />;
  }

  console.log('data.pages', data.pages);
  return (
    <>
      {data.pages.map((page, pageIndex) => {
        return (
          <Fragment key={pageIndex}>
            {page.data.contents.map((cookie) => {
              return (
                <CookieCard
                  key={cookie.createdAt}
                  cookieData={cookie}
                />
              );
            })}
          </Fragment>
        );
      })}
      <div ref={observerCard}>trigger next fetch</div>
      {isFetchingNextPage && <DataLoading />}
    </>
  );
};

export default UserCookies;
