import { useState } from 'react'
import styles from './ExpenseForm.module.css'

const CATEGORIES = ['Food', 'Transportation', 'Bills', 'Shopping', 'Others']

export default function ExpenseForm({ initial, onSubmit, onClose }) {
  const [form, setForm] = useState({
    amount:   initial?.amount   ?? '',
    category: initial?.category ?? 'Food',
    date:     initial?.date     ?? new Date().toISOString().split('T')[0],
    note:     initial?.note     ?? '',
  })
  const [error,  setError]  = useState('')
  const [saving, setSaving] = useState(false)

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')

    if (!form.amount || isNaN(Number(form.amount)) || Number(form.amount) <= 0) {
      setError('Amount must be a positive number.')
      return
    }
    if (!form.category) {
      setError('Category is required.')
      return
    }
    if (!form.date) {
      setError('Date is required.')
      return
    }

    try {
      setSaving(true)
      await onSubmit({ ...form, amount: Number(form.amount) })
    } catch (err) {
      setError(err.message || 'Something went wrong.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <div className={styles.header}>
          <h3>{initial ? 'Edit Expense' : 'Add Expense'}</h3>
          <button className={styles.closeBtn} onClick={onClose}>✕</button>
        </div>

        {error && <p className={styles.error}>{error}</p>}

        <form onSubmit={handleSubmit}>
          <label className={styles.label}>Amount (₱)</label>
          <input
            className={styles.input}
            name="amount"
            type="number"
            step="0.01"
            min="0.01"
            placeholder="0.00"
            value={form.amount}
            onChange={handleChange}
          />

          <label className={styles.label}>Category</label>
          <select
            className={styles.input}
            name="category"
            value={form.category}
            onChange={handleChange}
          >
            {CATEGORIES.map(c => (
              <option key={c} value={c}>{c}</option>
            ))}
          </select>

          <label className={styles.label}>Date</label>
          <input
            className={styles.input}
            name="date"
            type="date"
            value={form.date}
            onChange={handleChange}
          />

          <label className={styles.label}>Note (optional)</label>
          <input
            className={styles.input}
            name="note"
            placeholder="e.g. Lunch at canteen"
            value={form.note}
            onChange={handleChange}
          />

          <div className={styles.actions}>
            <button type="button" className={styles.cancelBtn} onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className={styles.saveBtn} disabled={saving}>
              {saving ? 'Saving...' : initial ? 'Save Changes' : 'Add Expense'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}