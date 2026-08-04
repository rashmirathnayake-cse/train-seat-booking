import { ArrowRight, CalendarDays, Map, MapPin, TrainFront, Users } from 'lucide-react'
import { Link } from 'react-router-dom'
import { PageHeader } from '../components/Ui'

const setup = [
  ['Create stations', 'Define every stop in the network', '/admin/stations'],
  ['Build a route', 'Add metadata and ordered stations', '/admin/routes'],
  ['Configure trains', 'Assign routes, coaches and seats', '/admin/trains'],
  ['Schedule a service', 'Generate stops and edit the timetable', '/admin/schedules'],
]
export default function DashboardPage() {
  return (
    <>
      <PageHeader
        eyebrow="Operations centre"
        title="Good morning, Admin"
        description="Set up and monitor your rail service from one workspace."
      />
      <div className="metric-grid">
        <div className="metric-card">
          <span className="metric-icon blue">
            <MapPin />
          </span>
          <div>
            <span>Stations</span>
            <strong>Network setup</strong>
            <small>Master data</small>
          </div>
        </div>
        <div className="metric-card">
          <span className="metric-icon violet">
            <Map />
          </span>
          <div>
            <span>Routes</span>
            <strong>Ordered stops</strong>
            <small>Configuration</small>
          </div>
        </div>
        <div className="metric-card">
          <span className="metric-icon green">
            <TrainFront />
          </span>
          <div>
            <span>Fleet</span>
            <strong>Trains & coaches</strong>
            <small>Seat generation</small>
          </div>
        </div>
        <div className="metric-card">
          <span className="metric-icon amber">
            <CalendarDays />
          </span>
          <div>
            <span>Schedules</span>
            <strong>Timetables</strong>
            <small>Publish services</small>
          </div>
        </div>
      </div>
      <div className="dashboard-grid dashboard-single">
        <section className="panel setup-panel">
          <div className="panel-head">
            <div>
              <span className="eyebrow">Recommended flow</span>
              <h2>Launch your first service</h2>
            </div>
            <span className="progress-pill">4 stages</span>
          </div>
          <div className="setup-list">
            {setup.map((s, i) => (
              <Link to={s[2]} key={s[0]}>
                <span className="step-number">{i + 1}</span>
                <div>
                  <strong>{s[0]}</strong>
                  <small>{s[1]}</small>
                </div>
                <ArrowRight />
              </Link>
            ))}
          </div>
        </section>
      </div>
      <section className="panel quick-panel">
        <div>
          <span className="metric-icon soft">
            <Users />
          </span>
          <div>
            <h3>Passenger booking comes next</h3>
            <p>
              Once a timetable is published, the public search and segment-aware seat booking flow
              can be added.
            </p>
          </div>
        </div>
        <Link to="/admin/bookings">
          View scope <ArrowRight size={16} />
        </Link>
      </section>
    </>
  )
}
