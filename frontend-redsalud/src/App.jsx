import React, { useState, useEffect, useContext } from 'react';
import api from './api/api';
import './App.css';
import { AuthContext } from './AuthContext'; // <-- IMPORTAMOS EL CONTEXTO
import Login from './Login'; // <-- IMPORTAMOS TU NUEVA PANTALLA

function App() {
  // Consumimos el estado global de autenticación
  const { isAuthenticated, logout } = useContext(AuthContext);

  const [patients, setPatients] = useState([]);
  const [requests, setRequests] = useState([]);
  const [waitingList, setWaitingList] = useState([]); 
  
  const [formData, setFormData] = useState({ 
    patientId: '', 
    specialty: '', 
    description: '',
    priority: '1', 
    type: 'request' 
  });

  const fetchData = async () => {
    try {
      const resP = await api.get('/patients');
      const resR = await api.get('/requests');
      const resW = await api.get('/waiting-list'); 
      
      setPatients(resP.data);
      setRequests(resR.data);
      setWaitingList(resW.data); 
    } catch (err) { 
      console.error("Error al cargar datos desde el Gateway", err); 
    }
  };

  // Solo ejecuta la carga de datos si el usuario está realmente autenticado
  useEffect(() => { 
    if (isAuthenticated) {
      fetchData(); 
    }
  }, [isAuthenticated]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (formData.type === 'request') {
        await api.post('/requests', {
          patientId: parseInt(formData.patientId),
          medicalSpecialty: formData.specialty,
          description: formData.description
        });
        alert("Solicitud creada con éxito");
      } else {
        await api.post('/waiting-list', null, {
          params: {
            patientId: parseInt(formData.patientId),
            medicalSpecialty: formData.specialty,
            priority: parseInt(formData.priority)
          }
        });
        alert("Paciente añadido a la Lista de Espera (201 Created)");
      }

      setFormData({ patientId: '', specialty: '', description: '', priority: '1', type: 'request' });
      fetchData(); 
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || "No se pudo procesar la acción"));
    }
  };

  // --- REGRESO DE GUARDIA ---
  // Si no está autenticado, la app se detiene aquí y muestra el Login de forma obligatoria
  if (!isAuthenticated) {
    return <Login />;
  }

  // Si está autenticado, renderiza el panel normal
  return (
    <div className="container">
      <header style={{ display: 'flex', justifyContent: 'between', alignItems: 'center', width: '100%' }}>
        <h1>RedSalud - Panel de Gestión</h1>
        <button onClick={logout} style={{ backgroundColor: '#ef4444', color: 'white', border: 'none', padding: '10px 15px', borderRadius: '8px', cursor: 'pointer', fontWeight: 'bold' }}>
          🚪 Cerrar Sesión
        </button>
      </header>

      {/* FORMULARIO */}
      <section className="card form-section">
        <h2>📝 Registrar Nueva Acción Médica</h2>
        <form onSubmit={handleSubmit} className="form-inline">
          <select value={formData.type} onChange={e => setFormData({...formData, type: e.target.value})}>
            <option value="request">Generar Solicitud</option>
            <option value="waiting">Ingresar a Lista de Espera</option>
          </select>

          <input type="number" placeholder="ID Paciente" value={formData.patientId} 
            onChange={e => setFormData({...formData, patientId: e.target.value})} required />
          
          <input type="text" placeholder="Especialidad" value={formData.specialty} 
            onChange={e => setFormData({...formData, specialty: e.target.value})} required />
          
          {formData.type === 'request' ? (
            <input type="text" placeholder="Descripción" value={formData.description} 
              onChange={e => setFormData({...formData, description: e.target.value})} required />
          ) : (
            <select value={formData.priority} onChange={e => setFormData({...formData, priority: e.target.value})}>
              <option value="1">Prioridad 1 (Alta)</option>
              <option value="2">Prioridad 2 (Media)</option>
              <option value="3">Prioridad 3 (Baja)</option>
            </select>
          )}

          <button type="submit">Enviar Registro</button>
        </form>
      </section>

      {/* GRID DE TABLAS */}
      <div className="grid" style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '20px' }}>
        <section className="card">
          <h2>🏥 Pacientes</h2>
          <table>
            <thead><tr><th>ID</th><th>Nombre</th><th>RUT</th></tr></thead>
            <tbody>
              {patients.map(p => (<tr key={p.id}><td>{p.id}</td><td>{p.firstName} {p.lastName}</td><td>{p.rut}</td></tr>))}
            </tbody>
          </table>
        </section>

        <section className="card">
          <h2>📑 Solicitudes</h2>
          <table>
            <thead><tr><th>Paciente</th><th>Especialidad</th><th>Estado</th></tr></thead>
            <tbody>
              {requests.map(r => (<tr key={r.id}><td>{r.patientId}</td><td>{r.medicalSpecialty}</td><td>{r.status}</td></tr>))}
            </tbody>
          </table>
        </section>

        <section className="card">
          <h2>⏳ Lista de Espera</h2>
          <table>
            <thead><tr><th>ID Paciente</th><th>Especialidad</th><th>Prioridad</th></tr></thead>
            <tbody>
              {waitingList.map(w => (
                <tr key={w.id}>
                  <td>{w.patientId}</td>
                  <td>{w.medicalSpecialty}</td>
                  <td>
                    <span className={`badge priority-${w.priority}`}>
                      {w.priority === 1 ? 'Alta' : w.priority === 2 ? 'Media' : 'Baja'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      </div>
    </div>
  );
}

export default App;