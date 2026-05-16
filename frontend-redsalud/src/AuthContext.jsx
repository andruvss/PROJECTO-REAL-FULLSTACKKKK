import React, { createContext, useState, useEffect } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Al cargar la app, revisamos si ya había una sesión guardada
  useEffect(() => {
    const loggedIn = localStorage.getItem('isLoggedIn') === 'true';
    setIsAuthenticated(loggedIn);
  }, []);

  const loginSimulado = (username, password) => {
    // Simulación rápida: acepta cualquier usuario y clave "1234"
    if (password === '1234') {
      localStorage.setItem('isLoggedIn', 'true');
      setIsAuthenticated(true);
      return true;
    }
    return false;
  };

  const logout = () => {
    localStorage.removeItem('isLoggedIn');
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, loginSimulado, logout }}>
      {children}
    </AuthContext.Provider>
  );
};