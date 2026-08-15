/** One dish in a chef's menu with a quantity stepper; parent owns the quantity state. */
function DishCard({ dish, quantity, onChange }) {
  const disabled = dish.available === false
  return (
    <div className={`dish-card ${disabled ? 'dish-card-disabled' : ''}`}>
      <div>
        <h5 className="mb-1">{dish.dishName}</h5>
        <p className="text-muted mb-0">₹{dish.pricePerUnit} / unit</p>
        {disabled && <small className="text-danger">Currently unavailable</small>}
      </div>
      <div className="qty-stepper">
        <button type="button" disabled={disabled || quantity === 0} onClick={() => onChange(Math.max(0, quantity - 1))}>-</button>
        <span>{quantity}</span>
        <button type="button" disabled={disabled} onClick={() => onChange(quantity + 1)}>+</button>
      </div>
    </div>
  )
}

export default DishCard
