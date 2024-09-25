import { Outlet } from 'react-router-dom';
import Header from './Header.jsx';
import Footer from './Footer.jsx';

const Layout = () => {
  return (
    <div className='h-full'>
      <Header />
      <main className='h-full'>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default Layout;
