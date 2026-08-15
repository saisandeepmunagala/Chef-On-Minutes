import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

/** Combined login/register page with a CUSTOMER/CHEF role toggle for signup. */
function Login() {
  const { login, register } = useAuth()
  const navigate = useNavigate()

  const [mode, setMode] = useState('login')
  const [form, setForm] = useState({ name: '', email: '', password: '', phone: '', role: 'CUSTOMER' })
  const [error, setError] = useState(null)
  const [submitting, setSubmitting] = useState(false)

  const update = (field) => (e) => setForm((prev) => ({ ...prev, [field]: e.target.value }))

  const handleSubmit = async (event) => {
    event.preventDefault()
    setSubmitting(true)
    setError(null)
    try {
      const user = mode === 'login'
        ? await login(form.email, form.password)
        : await register(form)
      navigate(user.role === 'CHEF' ? '/dashboard' : '/chefs')
    } catch (err) {
      setError(err.response?.data?.message || 'Something went wrong.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="container py-4" style={{ maxWidth: 420 }}>
      <div className="btn-group w-100 mb-4">
        <button type="button" className={`btn ${mode === 'login' ? 'btn-brand-primary' : 'btn-outline-secondary'}`} onClick={() => setMode('login')}>
          Login
        </button>
        <button type="button" className={`btn ${mode === 'register' ? 'btn-brand-primary' : 'btn-outline-secondary'}`} onClick={() => setMode('register')}>
          Sign up
        </button>
      </div>

      <form onSubmit={handleSubmit}>
        {mode === 'register' && (
          <>
            <input className="form-control mb-2" placeholder="Full name" value={form.name} onChange={update('name')} required />
            <input className="form-control mb-2" placeholder="Phone" value={form.phone} onChange={update('phone')} />
            <div className="mb-2">
              <label className="form-label fw-semibold">I am a</label>
              <select className="form-select" value={form.role} onChange={update('role')}>
                <option value="CUSTOMER">Customer</option>
                <option value="CHEF">Chef</option>
              </select>
            </div>
          </>
        )}
        <input type="email" className="form-control mb-2" placeholder="Email" value={form.email} onChange={update('email')} required />
        <input type="password" className="form-control mb-3" placeholder="Password" value={form.password} onChange={update('password')} required />
        {error && <p className="text-danger">{error}</p>}
        <button className="btn btn-brand-primary w-100" disabled={submitting} type="submit">
          {submitting ? 'Please wait...' : mode === 'login' ? 'Login' : 'Create account'}
        </button>
      </form>
    </div>
  )
}

export default Login

