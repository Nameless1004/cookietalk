import axios from 'axios';

const authInstance = axios.create({
  baseURL: 'http://localhost:8080/auth',
});

export default authInstance;
