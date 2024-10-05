import { Outlet } from 'react-router-dom';
import Header from './Header.jsx';
import Footer from './Footer.jsx';

const Layout = () => {
  return (
    <div className='flex flex-col justify-center items-center min-h-screen'>
      <Header />
      <main className='flex-1 w-full pt-[50px] px-[15%]'>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default Layout;
