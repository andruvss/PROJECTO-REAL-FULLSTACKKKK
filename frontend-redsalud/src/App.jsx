import React, { useState, useEffect } from 'react';
import api from './api/api';
import './App.css';

function App() {
  const [patients, setPatients] = useState([]);
  const [requests, setRequests] = useState([]);
  const [formData, setFormData] = useState({ patientId: '', specialty: '', description: '' });

  const fetchData = async () => {
    try {
      const resP = await api.get('/patients');
      const resR = await api.get('/requests');
      setPatients(resP.data);
      setRequests(resR.data);
    } catch (err) { console.error("Error al cargar", err); }
  };

  useEffect(() => { fetchData(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/requests', {
        patientId: parseInt(formData.patientId),
        medicalSpecialty: formData.specialty,
        description: formData.description
      });
      setFormData({ patientId: '', specialty: '', description: '' });
      fetchData(); // Recargar tablas
      alert("Solicitud creada con éxito");
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || "No se pudo crear"));
    }
  };

  return (
    <div className="container">
      <header><h1>RedSalud - Panel de Gestión</h1></header>

      {/* FORMULARIO NUEVO */}
      <section className="card form-section">
        <h2>📝 Nueva Solicitud Médica</h2>
        <form onSubmit={handleSubmit} className="form-inline">
          <input type="number" placeholder="ID Paciente" value={formData.patientId} 
            onChange={e => setFormData({...formData, patientId: e.target.value})} required />
          <input type="text" placeholder="Especialidad" value={formData.specialty} 
            onChange={e => setFormData({...formData, specialty: e.target.value})} required />
          <input type="text" placeholder="Descripción" value={formData.description} 
            onChange={e => setFormData({...formData, description: e.target.value})} required />
          <button type="submit">Enviar Solicitud</button>
        </form>
      </section>

      <div className="grid">
        <section className="card">
          <h2>🏥 Pacientes</h2>
          <table>
            <thead><tr><th>ID</th><th>Nombre</th><th>RUT</th></tr></thead>
            <tbody>
              {patients.map(p => (<tr key={p.id}><td>{p.id}</td><td>{p.name}</td><td>{p.rut}</td></tr>))}
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
      </div>
    </div>
  );
}

export default App;