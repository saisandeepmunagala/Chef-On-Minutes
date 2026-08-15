import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/')
  }

  return (
    <nav className="navbar navbar-expand-lg app-navbar">
      <div className="container">
        <Link className="navbar-brand app-brand" to="/">
          🍲 Chef On Minutes
        </Link>
        <div className="navbar-nav ms-auto d-flex flex-row gap-3 align-items-center">
          <Link className="nav-link" to="/chefs">Find a Chef</Link>
          {user?.role === 'CUSTOMER' && (
            <Link className="nav-link" to="/my-bookings">My Bookings</Link>
          )}
          {user?.role === 'CHEF' && (
            <Link className="nav-link" to="/dashboard">My Dashboard</Link>
          )}
          {user ? (
            <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>
              Logout ({user.name})
            </button>
          ) : (
            <Link className="btn btn-light btn-sm" to="/login">Login / Sign up</Link>
          )}
        </div>
      </div>
    </nav>
  )
}

export default Navbar
