import axios from 'axios';
import getAccessToken from '../../utilities/getAccessToken.js';

export const cookieInstance = axios.create({
  baseURL: 'http://localhost:8080/api/v1/cookies',
});

export const authCookieInstance = axios.create({
  baseURL: 'http://localhost:8080/api/v1/cookies',
});

authCookieInstance.interceptors.request.use(
  (config) => {
    const accessToken = getAccessToken();
    if (accessToken) {
      config.headers.Authorization = accessToken;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);
