import axios from 'axios';

const authInstance = axios.create({
  baseURL: import.meta.env.설정하기,
  headers: {
    // 필요한가 ??
    // Authorization: `Bearer ${token}`
  },
});

export default authInstance;
