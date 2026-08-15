import { useEffect, useState } from 'react'
import api from '../api/axiosConfig.js'
import { useAuth } from '../context/AuthContext.jsx'
import StatusBadge from '../components/StatusBadge.jsx'

/** Chef-only dashboard: manage own menu (ChefDish), own slots, and incoming bookings. */
function ChefDashboardPage() {
  const { user } = useAuth()
  const [tab, setTab] = useState('menu')
  const [chef, setChef] = useState(null)
  const [dishes, setDishes] = useState([])
  const [menu, setMenu] = useState([])
  const [slots, setSlots] = useState([])
  const [bookings, setBookings] = useState([])
  const [newDish, setNewDish] = useState({ dishId: '', pricePerUnit: '' })
  const [newSlot, setNewSlot] = useState({ date: '', startTime: '', endTime: '' })
  const [error, setError] = useState(null)

  const reloadMenu = (chefProfileId) => api.get(`/chefs/${chefProfileId}/dishes`).then((res) => setMenu(res.data))
  const reloadSlots = (chefProfileId) => api.get(`/chefs/${chefProfileId}/slots/all`).then((res) => setSlots(res.data))
  const reloadBookings = () => api.get(`/bookings/chef/${user.id}`).then((res) => setBookings(res.data))

  useEffect(() => {
    if (!user) return
    api.get('/chefs/me', { params: { chefUserId: user.id } }).then((res) => {
      setChef(res.data)
      reloadMenu(res.data.id)
      reloadSlots(res.data.id)
    }).catch(() => setError('Could not load your chef profile.'))
    api.get('/dishes').then((res) => setDishes(res.data))
    reloadBookings()
  }, [user])

  if (!user || user.role !== 'CHEF') {
    return <div className="container py-4"><p>This dashboard is only for chef accounts.</p></div>
  }
  if (!chef) return <div className="container py-4"><p>{error || 'Loading...'}</p></div>

  const handleAddDish = async (e) => {
    e.preventDefault()
    await api.post('/chefs/me/dishes', { dishId: Number(newDish.dishId), pricePerUnit: Number(newDish.pricePerUnit) }, { params: { chefUserId: user.id } })
    setNewDish({ dishId: '', pricePerUnit: '' })
    reloadMenu(chef.id)
  }

  const handleRemoveDish = async (chefDishId) => {
    await api.delete(`/chefs/me/dishes/${chefDishId}`, { params: { chefUserId: user.id } })
    reloadMenu(chef.id)
  }

  const handleAddSlot = async (e) => {
    e.preventDefault()
    await api.post('/chefs/me/slots', newSlot, { params: { chefUserId: user.id } })
    setNewSlot({ date: '', startTime: '', endTime: '' })
    reloadSlots(chef.id)
  }

  const advanceBooking = async (id, action) => {
    await api.patch(`/bookings/${id}/${action}`)
    reloadBookings()
  }

  return (
    <div className="container py-4">
      <h2>Welcome, {user.name}</h2>
      <ul className="nav nav-tabs my-3">
        {['menu', 'slots', 'bookings'].map((t) => (
          <li className="nav-item" key={t}>
            <button className={`nav-link ${tab === t ? 'active' : ''}`} onClick={() => setTab(t)}>
              {t === 'menu' ? 'My Menu' : t === 'slots' ? 'My Slots' : 'My Bookings'}
            </button>
          </li>
        ))}
      </ul>

      {tab === 'menu' && (
        <div>
          <form className="row g-2 mb-4" onSubmit={handleAddDish}>
            <div className="col-auto">
              <select className="form-select" value={newDish.dishId} onChange={(e) => setNewDish((p) => ({ ...p, dishId: e.target.value }))} required>
                <option value="">Choose a dish</option>
                {dishes.map((d) => <option key={d.id} value={d.id}>{d.name}</option>)}
              </select>
            </div>
            <div className="col-auto">
              <input className="form-control" type="number" placeholder="Price per unit" value={newDish.pricePerUnit}
                onChange={(e) => setNewDish((p) => ({ ...p, pricePerUnit: e.target.value }))} required />
            </div>
            <div className="col-auto">
              <button className="btn btn-brand-primary" type="submit">Add to menu</button>
            </div>
          </form>
          <ul className="list-group">
            {menu.map((m) => (
              <li key={m.id} className="list-group-item d-flex justify-content-between align-items-center">
                <span>{m.dishName} - ₹{m.pricePerUnit}</span>
                <button className="btn btn-sm btn-outline-danger" onClick={() => handleRemoveDish(m.id)}>Remove</button>
              </li>
            ))}
          </ul>
        </div>
      )}

      {tab === 'slots' && (
        <div>
          <form className="row g-2 mb-4" onSubmit={handleAddSlot}>
            <div className="col-auto">
              <input className="form-control" type="date" value={newSlot.date} onChange={(e) => setNewSlot((p) => ({ ...p, date: e.target.value }))} required />
            </div>
            <div className="col-auto">
              <input className="form-control" type="time" value={newSlot.startTime} onChange={(e) => setNewSlot((p) => ({ ...p, startTime: e.target.value }))} required />
            </div>
            <div className="col-auto">
              <input className="form-control" type="time" value={newSlot.endTime} onChange={(e) => setNewSlot((p) => ({ ...p, endTime: e.target.value }))} required />
            </div>
            <div className="col-auto">
              <button className="btn btn-brand-primary" type="submit">Open slot</button>
            </div>
          </form>
          <ul className="list-group">
            {slots.map((s) => (
              <li key={s.id} className="list-group-item d-flex justify-content-between">
                <span>{s.date} {s.startTime?.slice(0, 5)} - {s.endTime?.slice(0, 5)}</span>
                <StatusBadge status={s.status} />
              </li>
            ))}
          </ul>
        </div>
      )}

      {tab === 'bookings' && (
        <ul className="list-group">
          {bookings.map((b) => (
            <li key={b.id} className="list-group-item">
              <div className="d-flex justify-content-between">
                <span>{b.customerName} - {b.sessionDate} {b.startTime?.slice(0, 5)}</span>
                <StatusBadge status={b.status} />
              </div>
              <div className="mt-2 d-flex gap-2">
                {b.status === 'CONFIRMED' && <button className="btn btn-sm btn-outline-primary" onClick={() => advanceBooking(b.id, 'start')}>Start cooking</button>}
                {b.status === 'IN_PROGRESS' && <button className="btn btn-sm btn-outline-success" onClick={() => advanceBooking(b.id, 'complete')}>Mark completed</button>}
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

export default ChefDashboardPage
