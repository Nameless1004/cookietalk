import axios from 'axios';
import getAccessToken from '../../utilities/getAccessToken.js';

export const cookieInstance = axios.create({
  baseURL: 'http://localhost:8080/api/cookies',
});

export const authCookieInstance = axios.create({
  baseURL: 'http://localhost:8080/api/cookies',
});

authCookieInstance.interceptors.request.use(
  (config) => {
    const accessToken = getAccessToken();
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);
