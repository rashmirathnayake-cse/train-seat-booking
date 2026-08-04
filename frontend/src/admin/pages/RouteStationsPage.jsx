import { useState } from 'react'
import { ArrowLeft, GripVertical, MapPin, Plus, Trash2 } from 'lucide-react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { routeApi } from '../services/routeApi'
import { stationApi } from '../services/stationApi'
import { useApi } from '../hooks/useApi'
import ConfirmDialog from '../components/ConfirmDialog'
import { Empty, ErrorBanner, Field, Loading, Modal, PageHeader } from '../components/Ui'
export default function RouteStationsPage() {
  const { routeId } = useParams()
  const route = useLocation().state?.route
  const stops = useApi(() => routeApi.listStations(routeId), [routeId])
  const stations = useApi(stationApi.list, [])
  const [adding, setAdding] = useState(false)
  const [deleting, setDeleting] = useState(null)
  const [error, setError] = useState('')
  const [form, setForm] = useState({
    stationId: '',
    stopOrder: '',
    distanceFromOrigin: '',
    scheduledStop: true,
  })
  const add = async (e) => {
    e.preventDefault()
    try {
      await routeApi.addStation(routeId, {
        ...form,
        stationId: Number(form.stationId),
        stopOrder: Number(form.stopOrder),
        distanceFromOrigin: Number(form.distanceFromOrigin),
      })
      setAdding(false)
      setForm({ stationId: '', stopOrder: '', distanceFromOrigin: '', scheduledStop: true })
      stops.reload()
    } catch (err) {
      setError(err.message)
    }
  }
  return (
    <>
      <Link className="back-link" to="/admin/routes">
        <ArrowLeft />
        Back to routes
      </Link>
      <PageHeader
        eyebrow="Route builder"
        title={route?.name || `Route #${routeId}`}
        description="Add stations in the exact order the train will travel."
        actions={
          <button className="button primary" onClick={() => setAdding(true)}>
            <Plus />
            Add station
          </button>
        }
      />
      <ErrorBanner message={stops.error || error} />
      <div className="route-builder">
        <section className="panel">
          <div className="list-toolbar">
            <div>
              <h2>Ordered route stops</h2>
              <span>{stops.data.length} stops configured</span>
            </div>
          </div>
          {stops.loading ? (
            <Loading />
          ) : !stops.data.length ? (
            <Empty
              title="This route has no stations"
              text="Add the origin station first with stop order 1."
            />
          ) : (
            <div className="stop-list">
              {stops.data.map((s, i) => (
                <div className="stop-row" key={s.id}>
                  <GripVertical className="drag" />
                  <div className="route-line">
                    <i />
                    {i < stops.data.length - 1 && <span />}
                  </div>
                  <div className="stop-order">{s.stopOrder}</div>
                  <div className="stop-name">
                    <strong>{s.stationName}</strong>
                    <small>{s.distanceFromOrigin ?? 0} km from origin</small>
                  </div>
                  <span className="stop-type">
                    {s.scheduledStop ? 'Scheduled stop' : 'Pass through'}
                  </span>
                  <button className="icon-button danger-text" onClick={() => setDeleting(s)}>
                    <Trash2 />
                  </button>
                </div>
              ))}
            </div>
          )}
        </section>
        <aside className="panel route-guide">
          <span className="eyebrow">Route guidance</span>
          <h3>Keep the order reliable</h3>
          <p>Stop order powers passenger direction checks and segment-based seat availability.</p>
          <ul>
            <li>
              <strong>1</strong> starts at the route origin
            </li>
            <li>
              <strong>2+</strong> increases toward destination
            </li>
            <li>
              <strong>Distance</strong> must increase at each stop
            </li>
          </ul>
        </aside>
      </div>
      {adding && (
        <Modal
          title="Add route station"
          subtitle="Choose an existing station and place it in the route."
          onClose={() => setAdding(false)}
        >
          <ErrorBanner message={error} />
          <form onSubmit={add}>
            <div className="form-grid">
              <Field label="Station">
                <select
                  required
                  value={form.stationId}
                  onChange={(e) => setForm({ ...form, stationId: e.target.value })}
                >
                  <option value="">Select station</option>
                  {stations.data.map((s) => (
                    <option key={s.id} value={s.id}>
                      {s.name} ({s.code})
                    </option>
                  ))}
                </select>
              </Field>
              <Field label="Stop order">
                <input
                  required
                  min="1"
                  type="number"
                  value={form.stopOrder}
                  onChange={(e) => setForm({ ...form, stopOrder: e.target.value })}
                />
              </Field>
              <Field label="Distance from origin (km)">
                <input
                  required
                  min="0"
                  step="0.1"
                  type="number"
                  value={form.distanceFromOrigin}
                  onChange={(e) => setForm({ ...form, distanceFromOrigin: e.target.value })}
                />
              </Field>
              <label className="switch-field">
                <input
                  type="checkbox"
                  checked={form.scheduledStop}
                  onChange={(e) => setForm({ ...form, scheduledStop: e.target.checked })}
                />
                <span />
                <div>
                  <strong>Scheduled stop</strong>
                  <small>Train accepts passengers here</small>
                </div>
              </label>
            </div>
            <div className="modal-actions">
              <button type="button" className="button secondary" onClick={() => setAdding(false)}>
                Cancel
              </button>
              <button className="button primary">Add to route</button>
            </div>
          </form>
        </Modal>
      )}
      {deleting && (
        <ConfirmDialog
          title="Remove route station?"
          message={`Remove ${deleting.stationName} from this route?`}
          onCancel={() => setDeleting(null)}
          onConfirm={async () => {
            await routeApi.removeStation(deleting.id)
            setDeleting(null)
            stops.reload()
          }}
        />
      )}
    </>
  )
}
