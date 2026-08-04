import { Armchair } from 'lucide-react'

const seatNumberSort = new Intl.Collator(undefined, { numeric: true, sensitivity: 'base' })

export default function SeatMap({ seats, selectedSeatIds, onToggle }) {
  const orderedSeats = [...seats].sort((first, second) =>
    seatNumberSort.compare(String(first.seatNumber), String(second.seatNumber)),
  )

  return (
    <div className="visual-seat-map">
      <div className="coach-front">
        <span>Front of coach</span>
        <i />
      </div>
      <div className="seat-map-grid">
        {orderedSeats.map((seat, index) => (
          <button
            type="button"
            key={seat.seatId}
            className={`map-seat ${seat.available ? 'seat-free' : 'seat-taken'} ${selectedSeatIds.some((id) => String(id) === String(seat.seatId)) ? 'seat-selected' : ''} ${index % 6 === 3 ? 'after-aisle' : ''}`}
            disabled={!seat.available}
            onClick={() => onToggle(seat)}
            aria-label={`Seat ${seat.seatNumber}${seat.available ? ', available' : ', unavailable'}`}
          >
            <Armchair />
            <span>{seat.seatNumber}</span>
          </button>
        ))}
      </div>
      <div className="seat-map-legend">
        <span>
          <i className="legend-free" />
          Available
        </span>
        <span>
          <i className="legend-selected" />
          Selected
        </span>
        <span>
          <i className="legend-taken" />
          Unavailable
        </span>
      </div>
    </div>
  )
}
