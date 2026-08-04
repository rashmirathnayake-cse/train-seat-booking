import { ArrowLeft, Info } from 'lucide-react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { coachApi } from '../services/coachApi'
import { useApi } from '../hooks/useApi'
import { Empty, ErrorBanner, Loading, PageHeader } from '../components/Ui'
export default function SeatsPage() {
  const { trainId } = useParams()
  const train = useLocation().state?.train
  const coaches = useApi(() => coachApi.listByTrain(trainId), [trainId])
  return (
    <>
      <Link className="back-link" to="/admin/trains">
        <ArrowLeft />
        Back to trains
      </Link>
      <PageHeader
        eyebrow="Seat inventory"
        title={`${train?.trainName || train?.name || `Train #${trainId}`} seats`}
        description="Read-only seat inventory generated from each coach capacity."
      />
      <div className="info-banner">
        <Info />
        <span>
          The current API does not provide an admin seat-list endpoint. This view reflects the
          numbered seats the backend generates from each coach capacity.
        </span>
      </div>
      <ErrorBanner message={coaches.error} />
      {coaches.loading ? (
        <Loading />
      ) : !coaches.data.length ? (
        <section className="panel">
          <Empty title="No seats generated" text="Add coaches to this train first." />
        </section>
      ) : (
        <div className="coach-grid">
          {coaches.data.map((c) => (
            <section className="panel coach-seat-card" key={c.id}>
              <div className="coach-title">
                <div>
                  <span>Coach</span>
                  <h2>{c.coachNumber}</h2>
                </div>
                <div>
                  <strong>{c.seatCapacity}</strong>
                  <span>{c.type}</span>
                </div>
              </div>
              <div className="seat-grid">
                {Array.from({ length: c.seatCapacity }, (_, i) => (
                  <div className="seat available" key={i + 1}>
                    {i + 1}
                  </div>
                ))}
              </div>
              <div className="seat-legend">
                <span>
                  <i className="available" />
                  Generated
                </span>
                <small>Physical availability is segment-dependent at booking time.</small>
              </div>
            </section>
          ))}
        </div>
      )}
    </>
  )
}
