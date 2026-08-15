import { Link } from 'react-router-dom'

/**
 * Landing page: hero + tagline + value props + CTA to /chefs.
 */
function Home() {
  return (
    <div>
      <section className="hero">
        <div className="container text-center">
          <h1>Home-Cooked Meals, Made by Verified Chefs</h1>
          <p className="hero-tagline">Eat Healthy. Stay Away from Unhygienic Food.</p>
          <Link to="/chefs" className="btn btn-brand-primary btn-lg mt-3">Find a Chef</Link>
        </div>
      </section>

      <section className="container value-props">
        <div className="value-card">
          <div className="value-icon">🥗</div>
          <h4>Hygienic &amp; Fresh</h4>
          <p>Every chef cooks fresh, in your own kitchen, with ingredients you can see.</p>
        </div>
        <div className="value-card">
          <div className="value-icon">👨‍🍳</div>
          <h4>Verified Chefs</h4>
          <p>Rated and reviewed by real customers after every booking.</p>
        </div>
        <div className="value-card">
          <div className="value-icon">🏠</div>
          <h4>Home-Cooked Comfort</h4>
          <p>Pick a dish, pick a time slot, and enjoy lunch made just for you.</p>
        </div>
      </section>
    </div>
  )
}

export default Home

