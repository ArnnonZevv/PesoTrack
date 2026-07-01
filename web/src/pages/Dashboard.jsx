import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Dashboard() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/login')
  }

  return (
    <div className="dashboard-page">
      <header className="dashboard-header">
        <div className="brand">
          <span className="brand-mark">₱</span>
          <span className="brand-name">PesoTracker</span>
        </div>
        <button className="logout-btn" onClick={handleLogout}>
          Log out
        </button>
      </header>

      <main className="dashboard-main">
        <h1>Welcome, {user?.fullname || user?.username}!</h1>
        <p className="subtitle">You're logged in as <strong>{user?.username}</strong> ({user?.role}).</p>
        <div className="placeholder-card">
          <p>Expense tracking features go here in the next milestone.</p>
        </div>
      </main>
    </div>
  )
}
