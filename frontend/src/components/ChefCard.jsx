import { Link } from 'react-router-dom'
import RatingStars from './RatingStars.jsx'

/**
 * Single chef summary card used in the ChefList grid.
 */
function ChefCard({ chef }) {
  const prices = (chef.menu || []).map((d) => d.pricePerUnit).filter((p) => p != null)
  const priceRange = prices.length ? `₹${Math.min(...prices)} - ₹${Math.max(...prices)}` : 'Menu coming soon'

  return (
    <div className="chef-card">
      <div className="chef-card-avatar">{chef.name?.charAt(0) || '👨‍🍳'}</div>
      <h3>{chef.name}</h3>
      <p className="text-muted mb-1">{chef.specialty}</p>
      <RatingStars average={chef.ratingAvg || 0} count={chef.ratingCount || 0} />
      <p className="mb-2">{priceRange}</p>
      <Link className="btn btn-brand-primary btn-sm" to={`/chefs/${chef.id}`}>View Menu</Link>
    </div>
  )
}

export default ChefCard

