import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import axiosInstance from '../api/axiosInstance'
import ExpenseForm from '../components/ExpenseForm'
import ExpenseCard from '../components/ExpenseCard'
import styles from './Dashboard.module.css'

export default function Dashboard() {
  const nav = useNavigate()
  const fullname = localStorage.getItem('fullname') || 'User'

  const [expenses, setExpenses]   = useState([])
  const [loading, setLoading]     = useState(true)
  const [error, setError]         = useState('')
  const [showForm, setShowForm]   = useState(false)
  const [editing, setEditing]     = useState(null)   // null = add, object = edit

  useEffect(() => { fetchExpenses() }, [])

  async function fetchExpenses() {
    try {
      setLoading(true)
      const res = await axiosInstance.get('/api/expenses')
      setExpenses(res.data)
    } catch (err) {
      if (err.response?.status === 401) {
        handleLogout()
      } else {
        setError('Failed to load expenses.')
      }
    } finally {
      setLoading(false)
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this expense?')) return
    try {
      await axiosInstance.delete(`/api/expenses/${id}`)
      setExpenses(expenses.filter(e => e.id !== id))
    } catch {
      setError('Failed to delete expense.')
    }
  }

  function handleEdit(expense) {
    setEditing(expense)
    setShowForm(true)
  }

  function handleAdd() {
    setEditing(null)
    setShowForm(true)
  }

  async function handleFormSubmit(data) {
    try {
      if (editing) {
        const res = await axiosInstance.put(`/api/expenses/${editing.id}`, data)
        setExpenses(expenses.map(e => e.id === editing.id ? res.data : e))
      } else {
        const res = await axiosInstance.post('/api/expenses', data)
        setExpenses([res.data, ...expenses])
      }
      setShowForm(false)
      setEditing(null)
    } catch (err) {
      throw new Error(err.response?.data?.message || 'Failed to save expense.')
    }
  }

  function handleLogout() {
    localStorage.clear()
    nav('/login')
  }

  const total = expenses.reduce((sum, e) => sum + e.amount, 0)

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <div>
          <h1 className={styles.logo}>PesoTrack</h1>
          <p className={styles.welcome}>Hello, {fullname}</p>
        </div>
        <button className={styles.logoutBtn} onClick={handleLogout}>Log Out</button>
      </header>

      <main className={styles.main}>
        <div className={styles.summaryBar}>
          <div className={styles.summaryItem}>
            <span className={styles.summaryLabel}>Total Expenses</span>
            <span className={styles.summaryValue}>₱{total.toFixed(2)}</span>
          </div>
          <div className={styles.summaryItem}>
            <span className={styles.summaryLabel}>Records</span>
            <span className={styles.summaryValue}>{expenses.length}</span>
          </div>
          <button className={styles.addBtn} onClick={handleAdd}>+ Add Expense</button>
        </div>

        {error && <p className={styles.error}>{error}</p>}

        {loading ? (
          <p className={styles.empty}>Loading expenses...</p>
        ) : expenses.length === 0 ? (
          <p className={styles.empty}>No expenses yet. Tap "Add Expense" to start.</p>
        ) : (
          <div className={styles.list}>
            {expenses.map(e => (
              <ExpenseCard key={e.id} expense={e} onEdit={handleEdit} onDelete={handleDelete} />
            ))}
          </div>
        )}
      </main>

      {showForm && (
        <ExpenseForm
          initial={editing}
          onSubmit={handleFormSubmit}
          onClose={() => { setShowForm(false); setEditing(null) }}
        />
      )}
    </div>
  )
}