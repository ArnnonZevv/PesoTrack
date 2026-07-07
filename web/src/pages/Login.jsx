import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import axiosInstance from '../api/axiosInstance'
import styles from './Auth.module.css'

export default function Login() {
  const nav = useNavigate()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    if (!form.username || !form.password) {
      setError('Username and password are required.')
      return
    }
    try {
      setLoading(true)
      const res = await axiosInstance.post('/api/auth/login', form)
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('username', res.data.username)
      localStorage.setItem('fullname', res.data.fullname)
      nav('/')
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <h2 className={styles.title}>PesoTrack</h2>
        <p className={styles.subtitle}>Log in to your account</p>
        {error && <p className={styles.error}>{error}</p>}
        <form onSubmit={handleSubmit}>
          <input className={styles.input} name="username" placeholder="Username" value={form.username} onChange={handleChange} />
          <input className={styles.input} name="password" placeholder="Password" value={form.password} onChange={handleChange} type="password" />
          <button className={styles.btn} disabled={loading}>
            {loading ? 'Logging in...' : 'Log In'}
          </button>
        </form>
        <p className={styles.link}>No account? <Link to="/register">Register</Link></p>
      </div>
    </div>
  )
}