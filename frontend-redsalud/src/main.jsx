import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { AuthProvider } from './AuthContext';  //importamos el provider 

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider> {/* <-- ENVOLVEMOS LA APP */}
      <App />
    </AuthProvider>
  </StrictMode>,
)
