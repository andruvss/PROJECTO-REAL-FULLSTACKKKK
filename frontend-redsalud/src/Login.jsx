import React, { useState, useContext } from 'react';
import { AuthContext } from './AuthContext';

export default function Login() {
  const { loginReal } = useContext(AuthContext);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsSubmitting(true);
    
    // Llamamos al backend real de forma asíncrona
    const resultado = await loginReal(username, password);
    
    setIsSubmitting(false);
    if (!resultado.success) {
      setError(resultado.message);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.loginCard}>
        <div style={styles.header}>
          {/* Logo RedSalud Estilizado */}
          <div style={styles.logoContainer}>
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" fill="#007f8c"/>
            </svg>
            <h2 style={styles.title}>RedSalud <span style={styles.titleSpan}>Norte</span></h2>
          </div>
          <p style={styles.subtitle}>Panel de Gestión e Infraestructura Médica</p>
        </div>
        
        <form onSubmit={handleLoginSubmit} style={styles.form}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Usuario Clínico</label>
            <input 
              type="text" 
              placeholder="ej: medico_norte"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              style={styles.input}
              disabled={isSubmitting}
              required 
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Contraseña Institucional</label>
            <input 
              type="password" 
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={styles.input}
              disabled={isSubmitting}
              required 
            />
          </div>

          {error && (
            <div style={styles.errorBox}>
              <p style={styles.errorText}>⚠️ {error}</p>
            </div>
          )}

          <button 
            type="submit" 
            style={{
              ...styles.button,
              backgroundColor: isSubmitting ? '#a5f3fc' : '#007f8c'
            }}
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Autenticando...' : 'Iniciar Sesión'}
          </button>
        </form>
      </div>
    </div>
  );
}

const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: '#f8fafc' },
  loginCard: { background: '#fff', padding: '40px', borderRadius: '16px', border: '1px solid #e2e8f0', boxShadow: '0 10px 25px rgba(0,0,0,0.03)', width: '100%', maxWidth: '420px' },
  header: { textAlign: 'center', marginBottom: '32px' },
  logoContainer: { display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px', marginBottom: '6px' },
  title: { color: '#007f8c', margin: 0, fontSize: '26px', fontWeight: '700', letterSpacing: '-0.5px' },
  titleSpan: { color: '#95ca3e', fontWeight: '400' },
  subtitle: { color: '#64748b', margin: 0, fontSize: '14px', lineHeight: '1.4' },
  form: { display: 'flex', flexDirection: 'column', gap: '20px' },
  inputGroup: { display: 'flex', flexDirection: 'column', gap: '8px' },
  label: { fontSize: '13px', fontWeight: '600', color: '#475569', textTransform: 'uppercase', letterSpacing: '0.3px' },
  input: { padding: '12px 16px', borderRadius: '10px', border: '1px solid #e2e8f0', fontSize: '16px', outline: 'none', backgroundColor: '#f8fafc' },
  errorBox: { background: '#fef2f2', border: '1px solid #fca5a5', padding: '10px', borderRadius: '8px' },
  errorText: { color: '#b91c1c', fontSize: '13px', margin: 0, textAlign: 'center', fontWeight: '500' },
  button: { padding: '14px', borderRadius: '10px', border: 'none', color: '#fff', fontSize: '16px', fontWeight: '600', cursor: 'pointer', transition: 'background 0.2s ease' }
};