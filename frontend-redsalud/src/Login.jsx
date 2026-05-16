import React, { useState, useContext } from 'react';
import { AuthContext } from './AuthContext'; // Asegúrate de que la ruta sea correcta

export default function Login() {
  const { loginSimulado } = useContext(AuthContext);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLoginSubmit = (e) => {
    e.preventDefault();
    const exito = loginSimulado(username, password);
    
    if (!exito) {
      setError('Credenciales incorrectas. Pista: la clave es 1234');
    } else {
      setError('');
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.loginCard}>
        <div style={styles.header}>
          <h2 style={styles.title}>🏥 RedSalud</h2>
          <p style={styles.subtitle}>Panel de Gestión Médica</p>
        </div>
        
        <form onSubmit={handleLoginSubmit} style={styles.form}>
          <div style={styles.inputGroup}>
            <label style={styles.label}>Usuario o Email</label>
            <input 
              type="text" 
              placeholder="ej: medico@redsalud.cl"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              style={styles.input}
              required 
            />
          </div>

          <div style={styles.inputGroup}>
            <label style={styles.label}>Contraseña</label>
            <input 
              type="password" 
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={styles.input}
              required 
            />
          </div>

          {error && <p style={styles.errorText}>{error}</p>}

          <button type="submit" style={styles.button}>
            Iniciar Sesión
          </button>
        </form>
      </div>
    </div>
  );
}

// Estilos rápidos en línea para que se vea moderno sin romper tus CSS actuales
const styles = {
  container: { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: '#f1f5f9' },
  loginCard: { background: '#fff', padding: '40px', borderRadius: '16px', boxShadow: '0 10px 25px rgba(0,0,0,0.05)', width: '100%', maxWidth: '400px' },
  header: { textAlign: 'center', marginBottom: '30px' },
  title: { color: '#004080', margin: '0 0 5px 0', fontSize: '28px', fontFamily: 'serif' },
  subtitle: { color: '#64748b', margin: 0, fontSize: '14px' },
  form: { display: 'flex', flexDirection: 'column', gap: '20px' },
  inputGroup: { display: 'flex', flexDirection: 'column', gap: '8px' },
  label: { fontSize: '14px', fontWeight: '500', color: '#475569' },
  input: { padding: '12px', borderRadius: '8px', border: '1px solid #cbd5e1', fontSize: '16px', outline: 'none' },
  errorText: { color: '#ef4444', fontSize: '14px', margin: 0, textAlign: 'center' },
  button: { padding: '12px', borderRadius: '8px', border: 'none', backgroundColor: '#004080', color: '#fff', fontSize: '16px', fontWeight: 'bold', cursor: 'pointer', transition: 'background 0.2s' }
};