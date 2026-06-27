import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios'; // Usamos un axios directo para el login y evitar bucles con el interceptor

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  // Al cargar la app, revisamos si ya existe un token JWT válido guardado
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      setIsAuthenticated(true);
    }
    setLoading(false);
  }, []);

  // Login REAL conectado al microservicio auth-service a través del Gateway
  const loginReal = async (username, password) => {
    try {
      // Apuntamos al endpoint del Gateway que redirige al auth-service
      const response = await axios.post('http://localhost:8090/api/auth/login', {
        username: username,
        password: password
      });

      // Asumiendo que tu auth-service devuelve un objeto con { token: "eyJhbG..." } o similar
      if (response.data && response.data.token) {
        localStorage.setItem('token', response.data.token);
        setIsAuthenticated(true);
        return { success: true };
      }
      
      return { success: false, message: "No se recibió el token de acceso." };
    } catch (err) {
      console.error("Error en la autenticación centralizada:", err);
      return { 
        success: false, 
        message: err.response?.data?.message || "Credenciales inválidas o servidor caído." 
      };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
  };

  if (loading) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', color: '#007f8c', fontWeight: 'bold' }}>Cargando RedSalud Norte...</div>;
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, loginReal, logout }}>
      {children}
    </AuthContext.Provider>
  );
};