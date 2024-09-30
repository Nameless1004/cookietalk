import useUserStore from '../zustand/userStore.js';
import { Navigate, Outlet, useLocation } from 'react-router-dom';

const NonAuthenticatedRoute = () => {
  const { authenticatedUser } = useUserStore((state) => state);
  const { pathname } = useLocation();

  if (authenticatedUser) {
    return (
      <Navigate
        to='/channel'
        state={{ redirectedForm: pathname }}
      />
    );
  }

  return <Outlet />;
};

export default NonAuthenticatedRoute;
