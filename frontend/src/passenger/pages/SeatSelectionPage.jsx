import { AlertCircle, ArrowRight, CalendarDays, Clock3, MapPin, TrainFront } from 'lucide-react'
import { useEffect, useMemo, useState } from 'react'
import { Navigate, useLocation, useNavigate, useSearchParams } from 'react-router-dom'
import PassengerDetailsForm from '../components/PassengerDetailsForm'
import PassengerHeader from '../components/PassengerHeader'
import SeatMap from '../components/SeatMap'
import { passengerApi } from '../services/passengerApi'

export default function SeatSelectionPage() {
  const [params] = useSearchParams()
  const location = useLocation()
  const navigate = useNavigate()
  const scheduleId = params.get('scheduleId')
  const originStopId = params.get('originStopId')
  const destinationStopId = params.get('destinationStopId')
  const searchSchedule = location.state?.schedule
  const [seatMap, setSeatMap] = useState(null)
  const [activeCoachId, setActiveCoachId] = useState(null)
  const [selectedSeats, setSelectedSeats] = useState([])
  const [loading, setLoading] = useState(true)
  const [booking, setBooking] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    if (!scheduleId || !originStopId || !destinationStopId) return
    passengerApi
      .seatMap({ scheduleId, originStopId, destinationStopId })
      .then((data) => {
        setSeatMap(data)
        setActiveCoachId(data.coaches?.[0]?.coachId ?? null)
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }, [scheduleId, originStopId, destinationStopId])

  const activeCoach = useMemo(
    () => seatMap?.coaches?.find((coach) => String(coach.coachId) === String(activeCoachId)),
    [seatMap, activeCoachId],
  )

  if (!scheduleId || !originStopId || !destinationStopId) return <Navigate to="/" replace />

  const selectCoach = (coachId) => {
    setActiveCoachId(coachId)
  }

  const toggleSeat = (seat) => {
    setSelectedSeats((current) => {
      const exists = current.some((selected) => String(selected.seatId) === String(seat.seatId))
      if (exists) {
        return current.filter((selected) => String(selected.seatId) !== String(seat.seatId))
      }
      return [...current, { ...seat, coachNumber: activeCoach?.coachNumber }].sort(
        (first, second) =>
          String(first.coachNumber).localeCompare(String(second.coachNumber), undefined, {
            numeric: true,
          }) ||
          String(first.seatNumber).localeCompare(String(second.seatNumber), undefined, {
            numeric: true,
          }),
      )
    })
  }

  const createBooking = async (passenger) => {
    if (!selectedSeats.length) {
      setError('Select at least one available seat before confirming your booking.')
      return
    }
    setBooking(true)
    setError('')
    try {
      const confirmation = await passengerApi.createBooking({
        trainScheduleId: Number(scheduleId),
        seatIds: selectedSeats.map((seat) => Number(seat.seatId)),
        originStopId: Number(originStopId),
        destinationStopId: Number(destinationStopId),
        ...passenger,
      })
      navigate('/booking/confirmation', { replace: true, state: { booking: confirmation } })
    } catch (err) {
      setError(err.message)
      if (err.status === 409) {
        const refreshed = await passengerApi.seatMap({
          scheduleId,
          originStopId,
          destinationStopId,
        })
        setSeatMap(refreshed)
        setSelectedSeats([])
      }
    } finally {
      setBooking(false)
    }
  }

  const departureTime = searchSchedule?.departureTime?.slice(0, 5)
  const arrivalTime = searchSchedule?.arrivalTime?.slice(0, 5)

  return (
    <div className="booking-page">
      <PassengerHeader />
      <main className="booking-main">
        <div className="booking-progress">
          <span className="active">
            <i>1</i>Select seat
          </span>
          <b />
          <span>
            <i>2</i>Passenger details
          </span>
          <b />
          <span>
            <i>3</i>Confirmation
          </span>
        </div>
        {loading ? (
          <div className="seat-loading">
            <span className="search-loader" />
            <strong>Preparing your coach…</strong>
            <p>Checking live seat availability</p>
          </div>
        ) : !seatMap ? (
          <div className="seat-loading error">
            <AlertCircle />
            <strong>We could not load this train</strong>
            <p>{error}</p>
          </div>
        ) : (
          <>
            <section className="selected-journey-card">
              <div className="selected-train">
                <span>
                  <TrainFront />
                </span>
                <div>
                  <small>Selected train</small>
                  <strong>
                    {seatMap.trainNumber} · {seatMap.trainName}
                  </strong>
                </div>
              </div>
              <div className="selected-route">
                <div>
                  <strong>{departureTime || 'Departure'}</strong>
                  <span>{seatMap.originStation}</span>
                </div>
                <div className="selected-line">
                  <i />
                  <ArrowRight />
                  <i />
                </div>
                <div>
                  <strong>{arrivalTime || 'Arrival'}</strong>
                  <span>{seatMap.destinationStation}</span>
                </div>
              </div>
              <div className="selected-date">
                <CalendarDays />
                <div>
                  <small>Travel date</small>
                  <strong>
                    {new Date(`${seatMap.travelDate}T00:00:00`).toLocaleDateString(undefined, {
                      month: 'short',
                      day: 'numeric',
                      year: 'numeric',
                    })}
                  </strong>
                </div>
              </div>
            </section>
            {error && (
              <div className="booking-error">
                <AlertCircle />
                {error}
              </div>
            )}
            <div className="seat-booking-layout">
              <section className="seat-selection-panel">
                <div className="booking-section-heading">
                  <span>1</span>
                  <div>
                    <h1>Choose your seat</h1>
                    <p>Select one or more available seats from any reserved coach.</p>
                  </div>
                </div>
                <div className="coach-tabs">
                  {seatMap.coaches.map((coach) => (
                    <button
                      type="button"
                      key={coach.coachId}
                      className={String(coach.coachId) === String(activeCoachId) ? 'active' : ''}
                      onClick={() => selectCoach(coach.coachId)}
                    >
                      <small>Coach</small>
                      <strong>{coach.coachNumber}</strong>
                      <span>{coach.availableSeatCount} available</span>
                    </button>
                  ))}
                </div>
                {activeCoach ? (
                  <SeatMap
                    seats={activeCoach.seats}
                    selectedSeatIds={selectedSeats.map((seat) => seat.seatId)}
                    onToggle={toggleSeat}
                  />
                ) : (
                  <div className="no-reserved-coaches">
                    No reserved coaches are available on this train.
                  </div>
                )}
              </section>
              <aside className="booking-summary-panel">
                <div className="summary-title">
                  <span>Booking summary</span>
                  <small>One passenger</small>
                </div>
                <dl>
                  <div>
                    <dt>
                      <MapPin />
                      Journey
                    </dt>
                    <dd>
                      {seatMap.originStation}
                      <ArrowRight />
                      {seatMap.destinationStation}
                    </dd>
                  </div>
                  <div>
                    <dt>
                      <Clock3 />
                      Train
                    </dt>
                    <dd>
                      {seatMap.trainNumber} · {seatMap.trainName}
                    </dd>
                  </div>
                  <div>
                    <dt>Selected seats</dt>
                    <dd className={selectedSeats.length ? 'highlight selected-seat-summary' : ''}>
                      {selectedSeats.length
                        ? selectedSeats
                            .map((seat) => `${seat.coachNumber}-${seat.seatNumber}`)
                            .join(', ')
                        : 'Choose one or more seats'}
                    </dd>
                  </div>
                </dl>
                {searchSchedule?.estimatedFare != null && (
                  <div className="booking-total">
                    <span>Estimated fare</span>
                    <strong>
                      LKR{' '}
                      {(
                        Number(searchSchedule.estimatedFare) * selectedSeats.length
                      ).toLocaleString()}
                    </strong>
                  </div>
                )}
                {selectedSeats.length ? (
                  <PassengerDetailsForm onSubmit={createBooking} busy={booking} />
                ) : (
                  <div className="select-seat-prompt">
                    Select one or more green seats to continue
                  </div>
                )}
              </aside>
            </div>
          </>
        )}
      </main>
    </div>
  )
}
