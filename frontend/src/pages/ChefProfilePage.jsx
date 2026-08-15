import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import api from '../api/axiosConfig.js'
import { useAuth } from '../context/AuthContext.jsx'
import RatingStars from '../components/RatingStars.jsx'
import DishCard from '../components/DishCard.jsx'
import SlotPicker from '../components/SlotPicker.jsx'

/** Chef detail page: pick dishes + quantity, pick a slot, pick an address, then confirm. */
function ChefProfilePage() {
  const { chefId } = useParams()
  const { user } = useAuth()
  const navigate = useNavigate()

  const [chef, setChef] = useState(null)
  const [addresses, setAddresses] = useState([])
  const [slots, setSlots] = useState([])
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10))
  const [quantities, setQuantities] = useState({})
  const [slotId, setSlotId] = useState(null)
  const [addressId, setAddressId] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.get(`/chefs/${chefId}`).then((res) => setChef(res.data)).catch(() => setError('Chef not found.'))
  }, [chefId])

  useEffect(() => {
    if (user?.role === 'CUSTOMER') {
      api.get(`/users/${user.id}/addresses`).then((res) => setAddresses(res.data)).catch(() => {})
    }
  }, [user])

  useEffect(() => {
    api.get(`/chefs/${chefId}/slots`, { params: { date } }).then((res) => setSlots(res.data)).catch(() => setSlots([]))
    setSlotId(null)
  }, [chefId, date])

  if (error) return <div className="container py-4"><p className="text-danger">{error}</p></div>
  if (!chef) return <div className="container py-4"><p>Loading chef...</p></div>

  const total = (chef.menu || []).reduce((sum, dish) => sum + (quantities[dish.id] || 0) * dish.pricePerUnit, 0)
  const selectedItems = Object.entries(quantities)
    .filter(([, qty]) => qty > 0)
    .map(([chefDishId, quantity]) => ({ chefDishId: Number(chefDishId), quantity }))

  const canProceed = user?.role === 'CUSTOMER' && slotId && selectedItems.length > 0

  const handleProceed = () => {
    navigate('/booking/confirm', {
      state: { chefId: chef.id, slotId, addressId, items: selectedItems, chef, total },
    })
  }

  return (
    <div className="container py-4">
      <div className="chef-hero mb-4">
        <div className="chef-card-avatar chef-hero-avatar">{chef.name?.charAt(0)}</div>
        <div>
          <h2 className="mb-1">{chef.name}</h2>
          <p className="text-muted mb-1">{chef.specialty}</p>
          <RatingStars average={chef.ratingAvg || 0} count={chef.ratingCount || 0} />
          <p className="mt-2">{chef.bio}</p>
        </div>
      </div>

      <div className="row g-4">
        <div className="col-lg-7">
          <h4>Menu</h4>
          {(chef.menu || []).length === 0 && <p className="text-muted">This chef hasn't added any dishes yet.</p>}
          {(chef.menu || []).map((dish) => (
            <DishCard
              key={dish.id}
              dish={dish}
              quantity={quantities[dish.id] || 0}
              onChange={(qty) => setQuantities((prev) => ({ ...prev, [dish.id]: qty }))}
            />
          ))}
        </div>

        <div className="col-lg-5">
          <div className="booking-summary-card">
            <h4>Pick a slot</h4>
            <SlotPicker slots={slots} selectedSlotId={slotId} onSelect={setSlotId} date={date} onDateChange={setDate} />

            {user?.role === 'CUSTOMER' && (
              <>
                <label className="form-label fw-semibold mt-3">Delivery address</label>
                <select className="form-select mb-3" value={addressId || ''} onChange={(e) => setAddressId(Number(e.target.value) || null)}>
                  <option value="">Select an address</option>
                  {addresses.map((a) => (
                    <option key={a.id} value={a.id}>{a.line1}, {a.city}</option>
                  ))}
                </select>
              </>
            )}

            <hr />
            <div className="d-flex justify-content-between fw-bold">
              <span>Total</span>
              <span>₹{total}</span>
            </div>

            {!user && <p className="text-danger mt-2">Please login as a customer to book.</p>}
            <button className="btn btn-brand-primary w-100 mt-3" disabled={!canProceed} onClick={handleProceed}>
              Continue to Confirm
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ChefProfilePage
