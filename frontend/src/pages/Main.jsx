import Banner from '../components/main/Banner.jsx';
import CategoryLink from '../components/main/CategoryLink.jsx';
import COOKIE_CATEGORIES from '../constant/cookieCaterogies.js';

const Main = () => {
  return (
    <div className='flex flex-col justify-center items-center gap-10 mt-[50px] px-[15%]'>
      <Banner />
      <div className='w-full'>
        <h3 className='mb-[20px] font-bold text-[20px]'>어떤 분야에 관심이 있으신가요?</h3>
        <div className='flex justify-around items-center flex-wrap gap-5 w-full'>
          {COOKIE_CATEGORIES.map((category) => {
            return (
              <CategoryLink
                key={category.name}
                category={category}
              />
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default Main;
