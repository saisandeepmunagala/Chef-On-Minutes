import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home.jsx'
import ChefList from './pages/ChefList.jsx'
import ChefProfilePage from './pages/ChefProfilePage.jsx'
import BookingPage from './pages/BookingPage.jsx'
import BookingHistoryPage from './pages/BookingHistoryPage.jsx'
import ChefDashboardPage from './pages/ChefDashboardPage.jsx'
import Login from './pages/Login.jsx'
import Navbar from './components/Navbar.jsx'
import Footer from './components/Footer.jsx'
import { AuthProvider } from './context/AuthContext.jsx'

/**
 * Top-level route table + shared layout (Navbar/Footer).
 */
function App() {
  return (
    <AuthProvider>
      <Navbar />
      <main className="app-main">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/chefs" element={<ChefList />} />
          <Route path="/chefs/:chefId" element={<ChefProfilePage />} />
          <Route path="/booking/confirm" element={<BookingPage />} />
          <Route path="/my-bookings" element={<BookingHistoryPage />} />
          <Route path="/dashboard" element={<ChefDashboardPage />} />
          <Route path="/login" element={<Login />} />
        </Routes>
      </main>
      <Footer />
    </AuthProvider>
  )
}

export default App

