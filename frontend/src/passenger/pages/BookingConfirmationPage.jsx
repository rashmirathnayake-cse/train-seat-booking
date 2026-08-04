import {
  ArrowRight,
  CalendarDays,
  Check,
  Download,
  MapPin,
  TicketCheck,
  TrainFront,
} from 'lucide-react'
import { Link, Navigate, useLocation } from 'react-router-dom'
import PassengerHeader from '../components/PassengerHeader'

export default function BookingConfirmationPage() {
  const booking = useLocation().state?.booking
  if (!booking) return <Navigate to="/" replace />

  const bookingItems = Array.isArray(booking)
    ? booking
    : booking.bookings || booking.seats || booking.bookingSeats || []
  const primaryBooking = Array.isArray(booking) ? booking[0] : booking
  const bookedSeats = bookingItems.length
    ? bookingItems.map(
        (item) =>
          `${item.coachNumber || item.coach?.coachNumber || ''}-${item.seatNumber || item.seat?.seatNumber || ''}`,
      )
    : Array.isArray(primaryBooking.seatNumbers)
      ? primaryBooking.seatNumbers
      : [`${primaryBooking.coachNumber || ''}-${primaryBooking.seatNumber || ''}`]
  const seatDisplay = bookedSeats.map((seat) => String(seat).replace(/^-|-$/g, '')).join(', ')
  return (
    <div className="booking-page confirmation-page">
      <PassengerHeader backLabel="New booking" />
      <main className="confirmation-main">
        <div className="confirmation-check">
          <Check />
        </div>
        <span className="passenger-eyebrow">Booking confirmed</span>
        <h1>You’re all set for your journey!</h1>
        <p>
          Your {bookedSeats.length === 1 ? 'seat has' : 'seats have'} been reserved. Keep your
          booking reference somewhere safe.
        </p>
        <section className="ticket-card">
          <div className="ticket-top">
            <div>
              <small>Booking reference</small>
              <strong>
                {primaryBooking.bookingReference || primaryBooking.orderReference || 'Confirmed'}
              </strong>
            </div>
            <span>
              <TicketCheck />
            </span>
          </div>
          <div className="ticket-route">
            <div>
              <small>From</small>
              <strong>{primaryBooking.originStation}</strong>
            </div>
            <span>
              <i />
              <TrainFront />
              <i />
            </span>
            <div>
              <small>To</small>
              <strong>{primaryBooking.destinationStation}</strong>
            </div>
          </div>
          <div className="ticket-details">
            <div>
              <CalendarDays />
              <span>
                <small>Travel date</small>
                <strong>{primaryBooking.travelDate}</strong>
              </span>
            </div>
            <div>
              <TrainFront />
              <span>
                <small>Train</small>
                <strong>{primaryBooking.trainNumber}</strong>
              </span>
            </div>
            <div>
              <MapPin />
              <span>
                <small>{bookedSeats.length === 1 ? 'Coach / Seat' : 'Selected seats'}</small>
                <strong>{seatDisplay}</strong>
              </span>
            </div>
          </div>
          <div className="ticket-passenger">
            <span>Passenger</span>
            <strong>{primaryBooking.passengerName}</strong>
            <small>{primaryBooking.phone}</small>
          </div>
        </section>
        <div className="confirmation-actions">
          <button type="button" onClick={() => window.print()}>
            <Download />
            Print confirmation
          </button>
          <Link to="/">
            Book another journey <ArrowRight />
          </Link>
        </div>
      </main>
    </div>
  )
}
