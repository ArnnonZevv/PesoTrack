import styles from './ExpenseCard.module.css'

const CATEGORY_COLORS = {
  Food: '#e8f5e9',
  Transportation: '#e3f2fd',
  Bills: '#fce4ec',
  Shopping: '#fff3e0',
  Others: '#f3e5f5',
}

export default function ExpenseCard({ expense, onEdit, onDelete }) {
  const bgColor = CATEGORY_COLORS[expense.category] || '#f5f5f5'

  return (
    <div className={styles.card} style={{ borderLeft: `4px solid #1f3b57` }}>
      <div className={styles.left}>
        <span className={styles.category} style={{ background: bgColor }}>
          {expense.category}
        </span>
        <span className={styles.date}>{expense.date}</span>
        {expense.note && <span className={styles.note}>{expense.note}</span>}
      </div>
      <div className={styles.right}>
        <span className={styles.amount}>₱{Number(expense.amount).toFixed(2)}</span>
        <div className={styles.actions}>
          <button className={styles.editBtn}   onClick={() => onEdit(expense)}>Edit</button>
          <button className={styles.deleteBtn} onClick={() => onDelete(expense.id)}>Delete</button>
        </div>
      </div>
    </div>
  )
}