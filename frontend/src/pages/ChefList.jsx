import { useEffect, useState } from 'react'
import ChefCard from '../components/ChefCard.jsx'
import api from '../api/axiosConfig.js'

/**
 * Lists all chefs available for booking.
 */
function ChefList() {
  const [chefs, setChefs] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    api.get('/chefs')
      .then((res) => setChefs(res.data))
      .catch(() => setError('Could not load chefs right now.'))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="container py-4">
      <h2 className="mb-4">Available Chefs</h2>
      {loading && <p>Loading chefs...</p>}
      {error && <p className="text-danger">{error}</p>}
      {!loading && !error && chefs.length === 0 && <p>No chefs available yet.</p>}
      <div className="chef-grid">
        {chefs.map((chef) => (
          <ChefCard key={chef.id} chef={chef} />
        ))}
      </div>
    </div>
  )
}

export default ChefList

