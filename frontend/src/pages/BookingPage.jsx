import { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import api from '../api/axiosConfig.js'
import { useAuth } from '../context/AuthContext.jsx'

/** Confirm + pay step: creates the Booking, then pays for it. */
function BookingPage() {
  const { state } = useLocation()
  const navigate = useNavigate()
  const { user } = useAuth()
  const [mode, setMode] = useState('UPI')
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState(null)

  if (!state) {
    return (
      <div className="container py-4">
        <p>No booking selected. <a href="/chefs">Browse chefs</a> to start one.</p>
      </div>
    )
  }

  const { chef, slotId, addressId, items, total } = state

  const handleSubmit = async (event) => {
    event.preventDefault()
    setSubmitting(true)
    setError(null)
    try {
      const { data: booking } = await api.post('/bookings', {
        customerId: user.id,
        chefId: chef.userId,
        slotId,
        addressId,
        items,
      })
      await api.post('/payments', { bookingId: booking.id, mode })
      navigate('/my-bookings')
    } catch (err) {
      setError(err.response?.data?.message || 'Something went wrong while booking.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="container py-4" style={{ maxWidth: 480 }}>
      <h2>Confirm your booking</h2>
      <p className="text-muted">with {chef.name}</p>
      <ul className="list-group mb-3">
        {items.map((item) => (
          <li key={item.chefDishId} className="list-group-item d-flex justify-content-between">
            <span>Dish #{item.chefDishId} x{item.quantity}</span>
          </li>
        ))}
      </ul>
      <div className="d-flex justify-content-between fw-bold mb-3">
        <span>Total</span>
        <span>₹{total}</span>
      </div>

      <form onSubmit={handleSubmit}>
        <label className="form-label fw-semibold">Payment mode</label>
        <select className="form-select mb-3" value={mode} onChange={(e) => setMode(e.target.value)}>
          <option value="UPI">UPI</option>
          <option value="CARD">Card</option>
          <option value="CASH">Cash</option>
          <option value="WALLET">Wallet</option>
        </select>
        {error && <p className="text-danger">{error}</p>}
        <button className="btn btn-brand-primary w-100" disabled={submitting} type="submit">
          {submitting ? 'Booking...' : 'Pay & Confirm'}
        </button>
      </form>
    </div>
  )
}

export default BookingPage

