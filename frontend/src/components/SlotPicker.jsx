/** Grid of AVAILABLE slots for a chosen date; parent owns the selected slotId. */
function SlotPicker({ slots, selectedSlotId, onSelect, date, onDateChange }) {
  return (
    <div>
      <label className="form-label fw-semibold">Session date</label>
      <input
        type="date"
        className="form-control mb-3"
        value={date}
        min={new Date().toISOString().slice(0, 10)}
        onChange={(e) => onDateChange(e.target.value)}
      />
      {slots.length === 0 ? (
        <p className="text-muted">No available slots for this date.</p>
      ) : (
        <div className="slot-grid">
          {slots.map((slot) => (
            <button
              type="button"
              key={slot.id}
              className={`slot-chip ${selectedSlotId === slot.id ? 'slot-chip-selected' : ''}`}
              onClick={() => onSelect(slot.id)}
            >
              {slot.startTime?.slice(0, 5)} - {slot.endTime?.slice(0, 5)}
            </button>
          ))}
        </div>
      )}
    </div>
  )
}

export default SlotPicker
