import React, { useState, useEffect, useContext } from 'react';
import api from './api/api';
import './App.css';
import { AuthContext } from './AuthContext';
import Login from './Login';

function App() {
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

  // Función para formatear RUT de forma pro (12.345.678-K)
  const formatRut = (rawRut) => {
    if (!rawRut) return "N/A";
    let clean = rawRut.replace(/[^0-9kK]/g, '');
    if (clean.length < 2) return clean;
    let dv = clean.slice(-1).toUpperCase();
    let nums = clean.slice(0, -1);
    return nums.replace(/\B(?=(\d{3})+(?!\d))/g, ".") + "-" + dv;
  };

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
        // CORREGIDO: Se envían los datos en el Body como JSON en vez de pasarse por Parámetros (Query Params)
        await api.post('/waiting-list', {
          patientId: parseInt(formData.patientId),
          medicalSpecialty: formData.specialty,
          priority: parseInt(formData.priority)
        });
        alert("Paciente añadido a la Lista de Espera");
      }

      setFormData({ patientId: '', specialty: '', description: '', priority: '1', type: 'request' });
      fetchData(); 
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || "No se pudo procesar la acción"));
    }
  };

  if (!isAuthenticated) {
    return <Login />;
  }

  return (
    <>
      {/* BARRA DE NAVEGACIÓN CORPORATIVA REDSALUD NORTE */}
      <nav className="navbar">
        <div className="brand">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" fill="#007f8c"/>
          </svg>
          <h1>RedSalud <span>Norte</span></h1>
        </div>
        <button onClick={logout} className="btn-logout">
          Cerrar Sesión
        </button>
      </nav>

      <div className="container">
        {/* SECCIÓN DE REGISTRO */}
        <section className="card form-section">
          <h2 className="section-title">📝 Registrar Nueva Acción Médica</h2>
          <form onSubmit={handleSubmit} className="form-inline">
            <select 
              id="actionType"
              name="actionType"
              value={formData.type} 
              onChange={e => setFormData({...formData, type: e.target.value})}
            >
              <option value="request">Generar Solicitud</option>
              <option value="waiting">Ingresar a Lista de Espera</option>
            </select>

            <input 
              id="patientId"
              name="patientId"
              type="number" 
              placeholder="ID Paciente" 
              value={formData.patientId} 
              onChange={e => setFormData({...formData, patientId: e.target.value})} 
              required 
            />
            
            <input 
              id="specialty"
              name="specialty"
              type="text" 
              placeholder="Especialidad" 
              value={formData.specialty} 
              onChange={e => setFormData({...formData, specialty: e.target.value})} 
              required 
            />
            
            {formData.type === 'request' ? (
              <input 
                id="description"
                name="description"
                type="text" 
                placeholder="Descripción" 
                value={formData.description} 
                onChange={e => setFormData({...formData, description: e.target.value})} 
                required 
              />
            ) : (
              <select 
                id="priority"
                name="priority"
                value={formData.priority} 
                onChange={e => setFormData({...formData, priority: e.target.value})}
              >
                <option value="1">Prioridad 1 (Alta)</option>
                <option value="2">Prioridad 2 (Media)</option>
                <option value="3">Prioridad 3 (Baja)</option>
              </select>
            )}

            <button type="submit">Enviar Registro</button>
          </form>
        </section>

        {/* CONTENEDOR DASHBOARD UNIFICADO */}
        <div className="grid-dashboard">
          
          {/* TABLA PACIENTES */}
          <section className="card">
            <h2 className="section-title">🏥 Control de Pacientes</h2>
            <div className="table-responsive">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nombre Completo</th>
                    <th>RUT</th>
                  </tr>
                </thead>
                <tbody>
                  {patients.map(p => (
                    <tr key={p.id}>
                      <td><strong>{p.id}</strong></td>
                      <td>{p.firstName} {p.lastName}</td>
                      <td>
                        <span className="rut-chip">{formatRut(p.rut)}</span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>

          {/* TABLA SOLICITUDES */}
          <section className="card">
            <h2 className="section-title">📑 Solicitudes Activas</h2>
            <div className="table-responsive">
              <table>
                <thead>
                  <tr>
                    <th>ID Pac.</th>
                    <th>Especialidad Médica</th>
                    <th>Estado</th>
                  </tr>
                </thead>
                <tbody>
                  {requests.map(r => (
                    <tr key={r.id}>
                      <td>{r.patientId}</td>
                      <td>{r.medicalSpecialty}</td>
                      <td>
                        <span className="badge status-active">{r.status || 'PENDIENTE'}</span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>

          {/* TABLA LISTA DE ESPERA */}
          <section className="card">
            <h2 className="section-title">⏳ Lista de Espera</h2>
            <div className="table-responsive">
              <table>
                <thead>
                  <tr>
                    <th>ID Pac.</th>
                    <th>Especialidad Médica</th>
                    <th>Nivel Urgencia</th>
                  </tr>
                </thead>
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
            </div>
          </section>

        </div>
      </div>
    </>
  );
}

export default App;