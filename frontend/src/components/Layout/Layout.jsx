import { Outlet } from 'react-router-dom';
import Header from './Header.jsx';
import Footer from './Footer.jsx';

const Layout = () => {
  return (
    <div className='flex flex-col justify-center items-center h-full'>
      <Header />
      <main className='flex-1 w-full'>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default Layout;
