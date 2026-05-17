import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8090/api', // Puerto de BFF
});


// Este bloque adjunta el token JWT automáticamente si existe en el navegador
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token'); // O la forma en que lo guardes en tu AuthContext
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});


export default api;