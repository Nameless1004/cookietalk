import Main from '../pages/Main.jsx';
import Category from '../pages/Category.jsx';
import SearchResults from '../pages/SearchResults.jsx';
import NonAuthenticatedRoute from './NonAuthenticatedRoute.jsx';
import SignIn from '../pages/SignIn.jsx';
import SignUp from '../pages/SignUp.jsx';
import AuthenticatedRoute from './AuthenticatedRoute.jsx';
import Channel from '../pages/Channel.jsx';
import Cookie from '../pages/Cookie.jsx';
import PostCookie from '../pages/PostCookie.jsx';
import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom';
import Layout from '../components/Layout/Layout.jsx';

const Router = () => {
  const publicRoutes = [
    {
      path: '/',
      element: <Main />,
    },
    {
      path: 'category/:categoryId',
      element: <Category />,
    },
    {
      path: 'searchResults',
      element: <SearchResults />,
    },
  ];

  const routesForNonAuthenticatedOnly = [
    {
      path: '/',
      element: <NonAuthenticatedRoute />,
      children: [
        {
          path: 'signIn',
          element: <SignIn />,
        },
        {
          path: 'signUp',
          element: <SignUp />,
        },
      ],
    },
  ];

  const routesForAuthenticatedOnly = [
    {
      path: '/',
      element: <AuthenticatedRoute />,
      children: [
        {
          path: 'channel',
          element: <Channel />,
        },
        {
          path: 'cookie/:cookieId',
          element: <Cookie />,
        },
        {
          path: 'postCookie',
          element: <PostCookie />,
        },
      ],
    },
  ];

  const notFound = {
    path: '*',
    element: <Navigate to='/' />,
  };

  const router = createBrowserRouter([
    {
      path: '/',
      element: <Layout />,
      children: [...publicRoutes, ...routesForNonAuthenticatedOnly, ...routesForAuthenticatedOnly, notFound],
    },
  ]);

  return <RouterProvider router={router} />;
};

export default Router;
