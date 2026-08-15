import { useEffect, useState } from 'react'
import api from '../api/axiosConfig.js'
import { useAuth } from '../context/AuthContext.jsx'
import StatusBadge from '../components/StatusBadge.jsx'

/** Customer's own bookings: track status and leave a review once COMPLETED. */
function BookingHistoryPage() {
  const { user } = useAuth()
  const [bookings, setBookings] = useState([])
  const [reviewFor, setReviewFor] = useState(null)
  const [rating, setRating] = useState(5)
  const [comment, setComment] = useState('')

  const reload = () => api.get(`/bookings/customer/${user.id}`).then((res) => setBookings(res.data))

  useEffect(() => {
    if (user) reload()
  }, [user])

  if (!user || user.role !== 'CUSTOMER') {
    return <div className="container py-4"><p>Login as a customer to see your bookings.</p></div>
  }

  const cancelBooking = async (id) => {
    await api.patch(`/bookings/${id}/cancel`)
    reload()
  }

  const submitReview = async (bookingId) => {
    await api.post('/reviews', { bookingId, rating, comment })
    setReviewFor(null)
    setComment('')
    reload()
  }

  return (
    <div className="container py-4">
      <h2>My Bookings</h2>
      <ul className="list-group">
        {bookings.map((b) => (
          <li key={b.id} className="list-group-item">
            <div className="d-flex justify-content-between">
              <span>{b.chefName} - {b.sessionDate} {b.startTime?.slice(0, 5)}</span>
              <StatusBadge status={b.status} />
            </div>
            <p className="mb-1 text-muted">Total ₹{b.totalAmount} {b.paymentStatus && `(${b.paymentStatus})`}</p>
            <div className="d-flex gap-2">
              {(b.status === 'PENDING' || b.status === 'CONFIRMED') && (
                <button className="btn btn-sm btn-outline-danger" onClick={() => cancelBooking(b.id)}>Cancel</button>
              )}
              {b.status === 'COMPLETED' && (
                <button className="btn btn-sm btn-outline-secondary" onClick={() => setReviewFor(b.id)}>Leave a review</button>
              )}
            </div>
            {reviewFor === b.id && (
              <div className="mt-2">
                <select className="form-select mb-2" value={rating} onChange={(e) => setRating(Number(e.target.value))}>
                  {[5, 4, 3, 2, 1].map((r) => <option key={r} value={r}>{r} stars</option>)}
                </select>
                <textarea className="form-control mb-2" placeholder="Comment" value={comment} onChange={(e) => setComment(e.target.value)} />
                <button className="btn btn-sm btn-brand-primary" onClick={() => submitReview(b.id)}>Submit review</button>
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default BookingHistoryPage
