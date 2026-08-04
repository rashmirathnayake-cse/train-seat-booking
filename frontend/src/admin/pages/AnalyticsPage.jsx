import {
  Armchair,
  CalendarCheck,
  CircleDollarSign,
  RefreshCw,
  RotateCcw,
  ShoppingBag,
  TrendingUp,
} from 'lucide-react'
import { ErrorBanner, Loading, PageHeader } from '../components/Ui'
import { useApi } from '../hooks/useApi'
import { analyticsApi } from '../services/analyticsApi'

const money = (value) =>
  `LKR ${Number(value || 0).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`

export default function AnalyticsPage() {
  const { data, loading, error, reload } = useApi(analyticsApi.summary, [])
  const confirmed = Number(data.confirmedSeatBookings || 0)
  const cancelled = Number(data.cancelledSeatBookings || 0)
  const totalSeatBookings = confirmed + cancelled
  const confirmedRate = totalSeatBookings ? (confirmed / totalSeatBookings) * 100 : 0
  const cancelledRate = totalSeatBookings ? (cancelled / totalSeatBookings) * 100 : 0
  const occupancy = Math.min(100, Math.max(0, Number(data.averageOccupancyRate || 0)))

  return (
    <>
      <PageHeader
        eyebrow="Reporting"
        title="Analytics"
        description="A simple snapshot of bookings, revenue, schedules, and seat-leg occupancy."
        actions={
          <button className="button secondary" onClick={reload} disabled={loading}>
            <RefreshCw className={loading ? 'spin' : ''} /> Refresh
          </button>
        }
      />
      <ErrorBanner message={error} />
      {loading ? (
        <section className="panel">
          <Loading />
        </section>
      ) : (
        <>
          <div className="analytics-metrics">
            <article className="analytics-card">
              <span className="analytics-icon orders">
                <ShoppingBag />
              </span>
              <div>
                <small>Total orders</small>
                <strong>{Number(data.totalOrders || 0).toLocaleString()}</strong>
                <p>Distinct customer checkouts</p>
              </div>
            </article>
            <article className="analytics-card">
              <span className="analytics-icon confirmed">
                <Armchair />
              </span>
              <div>
                <small>Confirmed seats</small>
                <strong>{confirmed.toLocaleString()}</strong>
                <p>{confirmedRate.toFixed(1)}% of seat bookings</p>
              </div>
            </article>
            <article className="analytics-card">
              <span className="analytics-icon revenue">
                <CircleDollarSign />
              </span>
              <div>
                <small>Total revenue</small>
                <strong>{money(data.totalRevenue)}</strong>
                <p>Confirmed bookings only</p>
              </div>
            </article>
            <article className="analytics-card">
              <span className="analytics-icon schedules">
                <CalendarCheck />
              </span>
              <div>
                <small>Active schedules</small>
                <strong>{Number(data.activeSchedules || 0).toLocaleString()}</strong>
                <p>Currently published services</p>
              </div>
            </article>
          </div>

          <div className="analytics-detail-grid">
            <section className="panel occupancy-panel">
              <div className="analytics-panel-head">
                <div>
                  <span className="eyebrow">Network utilization</span>
                  <h2>Average occupancy</h2>
                </div>
                <span className="analytics-status">Active schedules</span>
              </div>
              <div className="occupancy-content">
                <div className="occupancy-gauge" style={{ '--occupancy': `${occupancy * 3.6}deg` }}>
                  <div>
                    <strong>{occupancy.toFixed(1)}%</strong>
                    <span>occupied seat-legs</span>
                  </div>
                </div>
                <div className="occupancy-copy">
                  <TrendingUp />
                  <h3>Segment-based measurement</h3>
                  <p>
                    This rate accounts for the same physical seat being reused across
                    non-overlapping journey segments.
                  </p>
                  <div className="occupancy-scale">
                    <span>0%</span>
                    <i>
                      <b style={{ width: `${occupancy}%` }} />
                    </i>
                    <span>100%</span>
                  </div>
                </div>
              </div>
            </section>

            <section className="panel booking-health-panel">
              <div className="analytics-panel-head">
                <div>
                  <span className="eyebrow">Reservation status</span>
                  <h2>Booking health</h2>
                </div>
              </div>
              <div className="booking-health-total">
                <strong>{totalSeatBookings.toLocaleString()}</strong>
                <span>Total seat bookings</span>
              </div>
              <div className="booking-health-bar">
                <i style={{ width: `${confirmedRate}%` }} />
                <b style={{ width: `${cancelledRate}%` }} />
              </div>
              <div className="booking-health-legend">
                <div>
                  <span className="confirmed-dot" />
                  <p>
                    <small>Confirmed</small>
                    <strong>{confirmed.toLocaleString()}</strong>
                  </p>
                </div>
                <div>
                  <span className="cancelled-dot" />
                  <p>
                    <small>Cancelled</small>
                    <strong>{cancelled.toLocaleString()}</strong>
                  </p>
                </div>
              </div>
            </section>
          </div>

          <section className="panel revenue-strip">
            <div className="revenue-strip-title">
              <span className="analytics-icon revenue">
                <CircleDollarSign />
              </span>
              <div>
                <span className="eyebrow">Revenue overview</span>
                <h2>Confirmed booking revenue</h2>
              </div>
            </div>
            <div className="revenue-value">
              <small>Today</small>
              <strong>{money(data.todayRevenue)}</strong>
            </div>
            <div className="revenue-divider" />
            <div className="revenue-value">
              <small>All time</small>
              <strong>{money(data.totalRevenue)}</strong>
            </div>
            <div className="revenue-note">
              <RotateCcw />
              <span>Cancelled seats are excluded</span>
            </div>
          </section>
        </>
      )}
    </>
  )
}
