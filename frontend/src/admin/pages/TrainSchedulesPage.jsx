import { useState } from 'react'
import { ArrowRight, CalendarDays, Edit3, Plus, Trash2 } from 'lucide-react'
import { Link } from 'react-router-dom'
import { scheduleApi } from '../services/scheduleApi'
import { trainApi } from '../services/trainApi'
import { useApi } from '../hooks/useApi'
import ConfirmDialog from '../components/ConfirmDialog'
import {
  Badge,
  Empty,
  ErrorBanner,
  Field,
  Loading,
  Modal,
  PageHeader,
  TableShell,
} from '../components/Ui'
function ScheduleForm({ initial, trains, onSubmit, onCancel }) {
  const [form, setForm] = useState({
    trainId: initial?.trainId || '',
    travelDate: initial?.travelDate || '',
    status: initial?.status || 'DRAFT',
  })
  const submit = (e) => {
    e.preventDefault()
    onSubmit({ ...form, trainId: Number(form.trainId) })
  }
  return (
    <form onSubmit={submit}>
      <div className="form-grid">
        <Field label="Train">
          <select
            required
            value={form.trainId}
            onChange={(e) => setForm({ ...form, trainId: e.target.value })}
          >
            <option value="">Select a train</option>
            {trains.map((t) => (
              <option key={t.trainId ?? t.id} value={t.trainId ?? t.id}>
                {t.trainNumber} — {t.trainName ?? t.name}
              </option>
            ))}
          </select>
        </Field>
        <Field label="Travel date">
          <input
            required
            type="date"
            value={form.travelDate}
            onChange={(e) => setForm({ ...form, travelDate: e.target.value })}
          />
        </Field>
        <Field label="Schedule status">
          <select
            value={form.status}
            onChange={(e) => setForm({ ...form, status: e.target.value })}
          >
            <option value="DRAFT">Draft</option>
            <option value="ACTIVE">Active / published</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
        </Field>
      </div>
      <div className="modal-actions">
        <button type="button" className="button secondary" onClick={onCancel}>
          Cancel
        </button>
        <button className="button primary">
          {initial ? 'Save changes' : 'Create & generate stops'}
        </button>
      </div>
    </form>
  )
}
export default function TrainSchedulesPage() {
  const list = useApi(scheduleApi.list, []),
    trains = useApi(trainApi.list, [])
  const [editing, setEditing] = useState(null),
    [deleting, setDeleting] = useState(null),
    [error, setError] = useState('')
  const trainName = (s) => {
    const t = trains.data.find((x) => Number(x.trainId ?? x.id) === Number(s.trainId))
    return s.trainNumber || t?.trainNumber || `Train #${s.trainId}`
  }
  const save = async (p) => {
    try {
      editing?.id ? await scheduleApi.update(editing.id, p) : await scheduleApi.create(p)
      setEditing(null)
      list.reload()
    } catch (e) {
      setError(e.message)
    }
  }
  return (
    <>
      <PageHeader
        eyebrow="Service planning"
        title="Train schedules"
        description="Create dated services, complete their timetables, then publish."
        actions={
          <button className="button primary" onClick={() => setEditing({})}>
            <Plus />
            Create schedule
          </button>
        }
      />
      <ErrorBanner message={list.error || error} />
      <div className="workflow-strip">
        <span className="done">
          1 <small>Create service</small>
        </span>
        <i />
        <span>
          2 <small>Edit timetable</small>
        </span>
        <i />
        <span>
          3 <small>Validate</small>
        </span>
        <i />
        <span>
          4 <small>Publish</small>
        </span>
      </div>
      <section className="panel">
        <div className="list-toolbar">
          <div>
            <h2>Service calendar</h2>
            <span>{list.data.length} scheduled services</span>
          </div>
        </div>
        {list.loading ? (
          <Loading />
        ) : !list.data.length ? (
          <Empty
            title="No services scheduled"
            text="Create a dated train run to generate its timetable stops."
          />
        ) : (
          <TableShell>
            <table>
              <thead>
                <tr>
                  <th>Service</th>
                  <th>Travel date</th>
                  <th>Status</th>
                  <th>Timetable</th>
                  <th className="actions-cell">Actions</th>
                </tr>
              </thead>
              <tbody>
                {list.data.map((s) => (
                  <tr key={s.id}>
                    <td>
                      <div className="entity">
                        <span className="entity-icon blue">
                          <CalendarDays />
                        </span>
                        <div>
                          <strong>Train {trainName(s)}</strong>
                          <small>Schedule ID #{s.id}</small>
                        </div>
                      </div>
                    </td>
                    <td>
                      {new Date(`${s.travelDate}T00:00:00`).toLocaleDateString(undefined, {
                        weekday: 'short',
                        month: 'short',
                        day: 'numeric',
                        year: 'numeric',
                      })}
                    </td>
                    <td>
                      <Badge
                        tone={
                          s.status === 'ACTIVE'
                            ? 'success'
                            : s.status === 'CANCELLED'
                              ? 'danger'
                              : 'warning'
                        }
                      >
                        {s.status}
                      </Badge>
                    </td>
                    <td>
                      <Link
                        className="text-link"
                        to={`/admin/schedules/${s.id}/timetable`}
                        state={{ schedule: s }}
                      >
                        Edit timetable <ArrowRight />
                      </Link>
                    </td>
                    <td className="actions-cell">
                      <button className="icon-button" onClick={() => setEditing(s)}>
                        <Edit3 />
                      </button>
                      <button className="icon-button danger-text" onClick={() => setDeleting(s)}>
                        <Trash2 />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </TableShell>
        )}
      </section>
      {editing && (
        <Modal
          title={editing.id ? 'Edit schedule' : 'Create train schedule'}
          subtitle="Route stops are generated automatically when the schedule is created."
          onClose={() => setEditing(null)}
        >
          <ErrorBanner message={error} />
          <ScheduleForm
            initial={editing.id ? editing : null}
            trains={trains.data}
            onSubmit={save}
            onCancel={() => setEditing(null)}
          />
        </Modal>
      )}
      {deleting && (
        <ConfirmDialog
          message="Delete this schedule and its generated stops? Prefer cancellation when bookings exist."
          onCancel={() => setDeleting(null)}
          onConfirm={async () => {
            try {
              await scheduleApi.remove(deleting.id)
              setDeleting(null)
              list.reload()
            } catch (e) {
              setError(e.message)
              setDeleting(null)
            }
          }}
        />
      )}
    </>
  )
}
