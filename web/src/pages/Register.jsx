import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import axiosInstance from '../api/axiosInstance'
import styles from './Auth.module.css'

export default function Register() {
  const nav = useNavigate()
  const [form, setForm] = useState({ fullname: '', email: '', username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')

    // Basic client-side validation
    if (!form.fullname || !form.email || !form.username || !form.password) {
      setError('All fields are required.')
      return
    }
    if (form.password.length < 6) {
      setError('Password must be at least 6 characters.')
      return
    }

    try {
      setLoading(true)
      const res = await axiosInstance.post('/api/auth/register', form)
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('username', res.data.username)
      localStorage.setItem('fullname', res.data.fullname)
      nav('/')
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <h2 className={styles.title}>PesoTrack</h2>
        <p className={styles.subtitle}>Create an account</p>
        {error && <p className={styles.error}>{error}</p>}
        <form onSubmit={handleSubmit}>
          <input className={styles.input} name="fullname"  placeholder="Full name"  value={form.fullname}  onChange={handleChange} />
          <input className={styles.input} name="email"     placeholder="Email"      value={form.email}     onChange={handleChange} type="email" />
          <input className={styles.input} name="username"  placeholder="Username"   value={form.username}  onChange={handleChange} />
          <input className={styles.input} name="password"  placeholder="Password"   value={form.password}  onChange={handleChange} type="password" />
          <button className={styles.btn} disabled={loading}>
            {loading ? 'Registering...' : 'Register'}
          </button>
        </form>
        <p className={styles.link}>Already have an account? <Link to="/login">Log in</Link></p>
      </div>
    </div>
  )
}