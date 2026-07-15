import { useState, useEffect, useMemo } from 'react'
import { useNavigate } from 'react-router-dom'
import axiosInstance from '../api/axiosInstance'
import ExpenseForm from '../components/ExpenseForm'
import ExpenseCard from '../components/ExpenseCard'
import styles from './Dashboard.module.css'

const CATEGORIES = ['All', 'Food', 'Transportation', 'Bills', 'Shopping', 'Others']

export default function Dashboard() {
  const nav      = useNavigate()
  const fullname = localStorage.getItem('fullname') || 'User'

  const [expenses, setExpenses] = useState([])
  const [loading,  setLoading]  = useState(true)
  const [error,    setError]    = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editing,  setEditing]  = useState(null)
  const [filter,   setFilter]   = useState('All')

  useEffect(() => { fetchExpenses() }, [])

  async function fetchExpenses() {
    try {
      setLoading(true)
      const res = await axiosInstance.get('/api/expenses')
      setExpenses(res.data)
    } catch (err) {
      if (err.response?.status === 401) handleLogout()
      else setError('Failed to load expenses.')
    } finally {
      setLoading(false)
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this expense?')) return
    try {
      await axiosInstance.delete(`/api/expenses/${id}`)
      setExpenses(prev => prev.filter(e => e.id !== id))
    } catch {
      setError('Failed to delete expense.')
    }
  }

  async function handleFormSubmit(data) {
    try {
      if (editing) {
        const res = await axiosInstance.put(`/api/expenses/${editing.id}`, data)
        setExpenses(prev => prev.map(e => e.id === editing.id ? res.data : e))
      } else {
        const res = await axiosInstance.post('/api/expenses', data)
        setExpenses(prev => [res.data, ...prev])
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

  // Filtered list — what actually shows in the cards
  const displayed = useMemo(() =>
    filter === 'All' ? expenses : expenses.filter(e => e.category === filter),
    [expenses, filter]
  )

  // Overall total (always from the full list)
  const grandTotal = expenses.reduce((sum, e) => sum + e.amount, 0)

  // Per-category breakdown (always from the full list)
  const breakdown = useMemo(() => {
    const map = {}
    expenses.forEach(e => {
      map[e.category] = (map[e.category] || 0) + e.amount
    })
    return map
  }, [expenses])

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

        {/* ── Summary bar ── */}
        <div className={styles.summaryBar}>
          <div className={styles.summaryItem}>
            <span className={styles.summaryLabel}>Grand Total</span>
            <span className={styles.summaryValue}>₱{grandTotal.toFixed(2)}</span>
          </div>
          <div className={styles.summaryItem}>
            <span className={styles.summaryLabel}>Records</span>
            <span className={styles.summaryValue}>{expenses.length}</span>
          </div>
          <button className={styles.addBtn} onClick={() => { setEditing(null); setShowForm(true) }}>
            + Add Expense
          </button>
        </div>

        {/* ── Per-category breakdown ── */}
        {expenses.length > 0 && (
          <div className={styles.breakdown}>
            <p className={styles.breakdownTitle}>By Category</p>
            <div className={styles.breakdownGrid}>
              {Object.entries(breakdown)
                .sort((a, b) => b[1] - a[1])
                .map(([cat, total]) => (
                  <div key={cat} className={styles.breakdownItem}>
                    <span className={styles.breakdownCat}>{cat}</span>
                    <span className={styles.breakdownAmt}>₱{total.toFixed(2)}</span>
                  </div>
                ))}
            </div>
          </div>
        )}

        {/* ── Category filter ── */}
        {expenses.length > 0 && (
          <div className={styles.filterRow}>
            <label className={styles.filterLabel}>Filter:</label>
            <div className={styles.filterBtns}>
              {CATEGORIES.map(cat => (
                <button
                  key={cat}
                  className={`${styles.filterBtn} ${filter === cat ? styles.filterBtnActive : ''}`}
                  onClick={() => setFilter(cat)}
                >
                  {cat}
                </button>
              ))}
            </div>
          </div>
        )}

        {/* ── Error ── */}
        {error && <p className={styles.error}>{error}</p>}

        {/* ── Expense list ── */}
        {loading ? (
          <p className={styles.empty}>Loading expenses...</p>
        ) : expenses.length === 0 ? (
          <p className={styles.empty}>No expenses yet. Tap "Add Expense" to start.</p>
        ) : displayed.length === 0 ? (
          <p className={styles.empty}>No expenses in this category.</p>
        ) : (
          <div className={styles.list}>
            {displayed.map(e => (
              <ExpenseCard
                key={e.id}
                expense={e}
                onEdit={exp => { setEditing(exp); setShowForm(true) }}
                onDelete={handleDelete}
              />
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