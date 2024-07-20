// axios.js
import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api', // The frontend server URL with the API prefix
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
  maxRedirects: 0,
});

export default axiosInstance;
