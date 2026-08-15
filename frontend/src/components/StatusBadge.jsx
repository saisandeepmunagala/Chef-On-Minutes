const COLORS = {
  PENDING: 'badge-pending',
  CONFIRMED: 'badge-confirmed',
  IN_PROGRESS: 'badge-progress',
  COMPLETED: 'badge-completed',
  CANCELLED: 'badge-cancelled',
}

/** Color-coded pill for a BookingStatus/PaymentStatus value. */
function StatusBadge({ status }) {
  return <span className={`status-badge ${COLORS[status] || ''}`}>{status}</span>
}

export default StatusBadge
