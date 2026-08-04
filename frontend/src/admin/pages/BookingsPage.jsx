import { Construction, TicketCheck } from 'lucide-react'
import { PageHeader } from '../components/Ui'
export default function BookingsPage() {
  return (
    <>
      <PageHeader
        eyebrow="Reservations"
        title="Bookings"
        description="Monitor passenger reservations and service occupancy."
      />
      <section className="panel coming-soon">
        <span className="coming-icon">
          <TicketCheck />
        </span>
        <span className="eyebrow">Backend endpoint required</span>
        <h2>Admin booking management is next</h2>
        <p>
          The current API contract supports public booking lookup and cancellation by reference, but
          does not define an admin booking-list endpoint. This page is ready to connect once that
          endpoint is available.
        </p>
        <div className="endpoint-note">
          <Construction />
          <div>
            <strong>Recommended endpoint</strong>
            <code>GET /api/admin/bookings?date=&amp;scheduleId=&amp;status=</code>
          </div>
        </div>
      </section>
    </>
  )
}
