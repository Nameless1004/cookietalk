import { Suspense } from 'react';
import DataLoading from '../common/DataLoading.jsx';

const ChannelCookieRegion = ({ title, children }) => {
  return (
    <div>
      <h2 className='font-bold text-xl'>{title}</h2>
      <div className='w-full h-[250px] overflow-x-auto flex items-center gap-3 overflow-auto'>
        <Suspense fallback={<DataLoading />}>{children}</Suspense>
      </div>
    </div>
  );
};

export default ChannelCookieRegion;
