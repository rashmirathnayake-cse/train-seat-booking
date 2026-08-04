import { ArrowRight, Armchair, Clock3, MapPin, TrainFront } from 'lucide-react'

const time = (value) => (value ? value.slice(0, 5) : '—')
const duration = (minutes) => {
  if (minutes == null) return '—'
  const hours = Math.floor(minutes / 60)
  const rest = minutes % 60
  return `${hours ? `${hours}h ` : ''}${rest}m`
}

export default function ScheduleResults({ results, searched, loading, onSelect }) {
  if (loading) {
    return (
      <div className="passenger-state">
        <span className="search-loader" />
        <strong>Finding the best trains for you…</strong>
      </div>
    )
  }
  if (!searched) return null
  if (!results.length) {
    return (
      <div className="passenger-state">
        <TrainFront />
        <strong>No trains found for this journey</strong>
        <p>Try another date, time, or station combination.</p>
      </div>
    )
  }
  return (
    <section className="schedule-results">
      <div className="results-heading">
        <div>
          <span className="passenger-eyebrow">Available journeys</span>
          <h2>
            {results.length} {results.length === 1 ? 'train' : 'trains'} found
          </h2>
        </div>
        <span>Prices shown are estimates</span>
      </div>
      <div className="result-list">
        {results.map((result) => (
          <article className="journey-card" key={result.scheduleId}>
            <div className="journey-train">
              <span>
                <TrainFront />
              </span>
              <div>
                <small>Train {result.trainNumber}</small>
                <strong>{result.trainName}</strong>
              </div>
            </div>
            <div className="journey-time">
              <div>
                <strong>{time(result.departureTime)}</strong>
                <small>Departure</small>
              </div>
              <div className="journey-duration">
                <span>
                  <i />
                </span>
                <small>
                  <Clock3 />
                  {duration(result.durationMinutes)}
                </small>
              </div>
              <div className="arrival">
                <strong>{time(result.arrivalTime)}</strong>
                <small>Arrival</small>
              </div>
            </div>
            <div className="journey-meta">
              <span>
                <MapPin />
                {result.journeyDistance ?? '—'} km
              </span>
              <span>
                <Armchair />
                {result.availableSeatCount ?? '—'} seats
              </span>
            </div>
            <div className="journey-price">
              <small>From</small>
              <strong>LKR {Number(result.estimatedFare ?? 0).toLocaleString()}</strong>
              <button type="button" onClick={() => onSelect(result)}>
                Select <ArrowRight />
              </button>
            </div>
          </article>
        ))}
      </div>
    </section>
  )
}
