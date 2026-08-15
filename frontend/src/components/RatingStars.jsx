/** Renders a 1-5 star rating, e.g. 4.3 -> ★★★★☆ with the numeric average shown alongside. */
function RatingStars({ average = 0, count = 0 }) {
  const rounded = Math.round(average)
  return (
    <span className="rating-stars" title={`${average.toFixed(1)} / 5 (${count} reviews)`}>
      {'★'.repeat(rounded)}
      {'☆'.repeat(5 - rounded)}
      <span className="rating-count"> ({count})</span>
    </span>
  )
}

export default RatingStars
