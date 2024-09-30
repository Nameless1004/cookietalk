import axios from 'axios';

const authInstance = axios.create({
  baseURL: 'http://localhost:8080/api/users',
});

export default authInstance;
