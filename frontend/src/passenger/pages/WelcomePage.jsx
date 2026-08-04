import {
  ArrowRight,
  CalendarDays,
  Clock3,
  Headphones,
  Leaf,
  Menu,
  Search,
  ShieldCheck,
  Sparkles,
  TicketCheck,
  TrainFront,
  UserRound,
  X,
} from 'lucide-react'
import { useEffect, useMemo, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import StationSelect from '../components/StationSelect'
import ScheduleResults from '../components/ScheduleResults'
import { passengerApi } from '../services/passengerApi'

const localDate = () => {
  const date = new Date()
  date.setMinutes(date.getMinutes() - date.getTimezoneOffset())
  return date.toISOString().slice(0, 10)
}

export default function WelcomePage() {
  const navigate = useNavigate()
  const [stations, setStations] = useState([])
  const [stationsLoading, setStationsLoading] = useState(true)
  const [form, setForm] = useState({
    originStationId: '',
    destinationStationId: '',
    travelDate: localDate(),
    departureAfter: '06:00',
  })
  const [results, setResults] = useState([])
  const [searching, setSearching] = useState(false)
  const [searched, setSearched] = useState(false)
  const [error, setError] = useState('')
  const [menuOpen, setMenuOpen] = useState(false)

  useEffect(() => {
    passengerApi
      .stations()
      .then(setStations)
      .catch((err) => setError(err.message))
      .finally(() => setStationsLoading(false))
  }, [])

  const origin = useMemo(
    () => stations.find((station) => String(station.id) === String(form.originStationId)),
    [stations, form.originStationId],
  )
  const destination = useMemo(
    () => stations.find((station) => String(station.id) === String(form.destinationStationId)),
    [stations, form.destinationStationId],
  )

  const swapStations = () => {
    setForm((current) => ({
      ...current,
      originStationId: current.destinationStationId,
      destinationStationId: current.originStationId,
    }))
  }

  const searchSchedules = async (event) => {
    event.preventDefault()
    setError('')
    if (!form.originStationId || !form.destinationStationId) {
      setError('Choose both your departure and destination stations.')
      return
    }
    if (String(form.originStationId) === String(form.destinationStationId)) {
      setError('Departure and destination stations must be different.')
      return
    }
    setSearching(true)
    setSearched(true)
    try {
      setResults(await passengerApi.searchSchedules(form))
      requestAnimationFrame(() =>
        document.getElementById('search-results')?.scrollIntoView({ behavior: 'smooth' }),
      )
    } catch (err) {
      setResults([])
      setError(err.message)
    } finally {
      setSearching(false)
    }
  }

  return (
    <div className="passenger-page">
      <header className="passenger-nav">
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
        <nav className={menuOpen ? 'open' : ''}>
          <a href="#find-trains" onClick={() => setMenuOpen(false)}>
            Book a train
          </a>
          <a href="#why-railease" onClick={() => setMenuOpen(false)}>
            Why RailEase
          </a>
          <a href="#help" onClick={() => setMenuOpen(false)}>
            Help
          </a>
          <Link to="/admin" onClick={() => setMenuOpen(false)}>
            Admin portal
          </Link>
        </nav>
        <div className="passenger-nav-actions">
          <button className="nav-login">
            <UserRound />
            My bookings
          </button>
          <button className="passenger-menu" onClick={() => setMenuOpen((open) => !open)}>
            {menuOpen ? <X /> : <Menu />}
          </button>
        </div>
      </header>

      <main className="passenger-main">
        <section className="passenger-hero" id="find-trains">
          <div className="hero-copy">
            <span className="hero-kicker">
              <Sparkles /> Your journey starts here
            </span>
            <h1>
              Discover Sri Lanka,
              <br />
              <em>one journey at a time.</em>
            </h1>
            <p>
              Search train schedules, compare journeys, and reserve your seat—all in one simple
              place.
            </p>
            <div className="hero-trust">
              <span>
                <ShieldCheck />
                Secure booking
              </span>
              <span>
                <TicketCheck />
                Instant confirmation
              </span>
            </div>
          </div>
          <div className="hero-visual" aria-hidden="true">
            <div className="sun" />
            <div className="cloud cloud-one" />
            <div className="cloud cloud-two" />
            <div className="mountain mountain-back" />
            <div className="mountain mountain-front" />
            <div className="rail-track">
              <i />
              <i />
            </div>
            <div className="hero-train">
              <span className="engine">
                <i />
                <i />
                <b />
              </span>
              <span />
              <span />
            </div>
            <div className="palm">
              <i />
              <b />
              <b />
              <b />
              <b />
            </div>
          </div>
        </section>

        <section className="search-card">
          <div className="search-card-heading">
            <div>
              <span className="passenger-eyebrow">Plan your journey</span>
              <h2>Where would you like to go?</h2>
            </div>
            <span className="live-schedules">
              <i /> Live schedules
            </span>
          </div>
          <form onSubmit={searchSchedules}>
            <div className="station-pair">
              <StationSelect
                label="From"
                placeholder="Select departure"
                stations={stations}
                value={form.originStationId}
                disabled={stationsLoading}
                onChange={(value) => setForm({ ...form, originStationId: value })}
              />
              <button
                type="button"
                className="swap-stations"
                onClick={swapStations}
                aria-label="Swap stations"
              >
                <ArrowRight />
                <ArrowRight />
              </button>
              <StationSelect
                label="To"
                placeholder="Select destination"
                stations={stations}
                value={form.destinationStationId}
                disabled={stationsLoading}
                onChange={(value) => setForm({ ...form, destinationStationId: value })}
              />
            </div>
            <label className="journey-field">
              <span>Date</span>
              <div>
                <CalendarDays />
                <input
                  type="date"
                  min={localDate()}
                  required
                  value={form.travelDate}
                  onChange={(event) => setForm({ ...form, travelDate: event.target.value })}
                />
              </div>
            </label>
            <label className="journey-field">
              <span>Depart after</span>
              <div>
                <Clock3 />
                <input
                  type="time"
                  required
                  value={form.departureAfter}
                  onChange={(event) => setForm({ ...form, departureAfter: event.target.value })}
                />
              </div>
            </label>
            <button className="find-trains-button" disabled={searching || stationsLoading}>
              <Search />
              {searching ? 'Searching…' : 'Find trains'}
              <ArrowRight />
            </button>
          </form>
          {error && <div className="passenger-error">{error}</div>}
          {origin && destination && (
            <div className="route-summary">
              <span>{origin.name}</span>
              <i />
              <ArrowRight />
              <i />
              <span>{destination.name}</span>
            </div>
          )}
        </section>

        <div id="search-results">
          <ScheduleResults
            results={results}
            searched={searched}
            loading={searching}
            onSelect={(schedule) => {
              const params = new URLSearchParams({
                scheduleId: schedule.scheduleId,
                originStopId: schedule.originStopId,
                destinationStopId: schedule.destinationStopId,
              })
              navigate(`/booking/seats?${params.toString()}`, { state: { schedule } })
            }}
          />
        </div>

        <section className="passenger-benefits" id="why-railease">
          <div>
            <span>
              <ShieldCheck />
            </span>
            <div>
              <strong>Safe & reliable</strong>
              <p>Accurate schedules and secure reservations.</p>
            </div>
          </div>
          <div>
            <span>
              <Leaf />
            </span>
            <div>
              <strong>A greener way to travel</strong>
              <p>See more of Sri Lanka with a lighter footprint.</p>
            </div>
          </div>
          <div id="help">
            <span>
              <Headphones />
            </span>
            <div>
              <strong>Here when you need us</strong>
              <p>Simple support throughout your journey.</p>
            </div>
          </div>
        </section>
      </main>
      <footer className="passenger-footer">
        <span>© 2026 RailEase</span>
        <span>Made for journeys across Sri Lanka.</span>
      </footer>
    </div>
  )
}
