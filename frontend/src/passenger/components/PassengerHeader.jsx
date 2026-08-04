import { ArrowLeft, TrainFront } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function PassengerHeader({ backTo = '/', backLabel = 'Back to search' }) {
  return (
    <header className="booking-header">
      <Link className="passenger-brand" to="/">
        <span>
          <TrainFront />
        </span>
        <div>
          <strong>
            Rail<span>Ease</span>
          </strong>
          <small>Sri Lanka Railways</small>
        </div>
      </Link>
      <Link className="booking-back" to={backTo}>
        <ArrowLeft />
        {backLabel}
      </Link>
    </header>
  )
}
