import useUserStore from '../zustand/userStore.js';
import { Navigate, Outlet, useLocation } from 'react-router-dom';

const AuthenticatedRoute = () => {
  const { authenticatedUser } = useUserStore((state) => state);
  const { pathname } = useLocation();

  if (!authenticatedUser) {
    return (
      <Navigate
        to='/signIn'
        replace
        stat={{ redirectedForm: pathname }}
      />
    );
  }

  return <Outlet />;
};

export default AuthenticatedRoute;
