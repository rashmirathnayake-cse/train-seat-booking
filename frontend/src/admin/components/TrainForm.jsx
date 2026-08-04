import { useState } from 'react'
import { Field } from './Ui'
export default function TrainForm({ initial, routes, onSubmit, onCancel }) {
  const [form, setForm] = useState({
    trainNumber: initial?.trainNumber || '',
    name: initial?.name ?? initial?.trainName ?? '',
    description: initial?.description || '',
    routeId: initial?.routeId ?? initial?.routId ?? '',
    active: initial?.active ?? true,
  })
  const submit = (e) => {
    e.preventDefault()
    onSubmit({ ...form, routeId: Number(form.routeId) })
  }
  return (
    <form onSubmit={submit}>
      <div className="form-grid two">
        <Field label="Train number">
          <input
            required
            value={form.trainNumber}
            onChange={(e) => setForm({ ...form, trainNumber: e.target.value })}
            placeholder="1005"
          />
        </Field>
        <Field label="Train name">
          <input
            required
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            placeholder="Udarata Menike"
          />
        </Field>
        <Field label="Assigned route">
          <select
            required
            value={form.routeId}
            onChange={(e) => setForm({ ...form, routeId: e.target.value })}
          >
            <option value="">Select a route</option>
            {routes.map((r) => (
              <option key={r.id} value={r.id}>
                {r.name}
              </option>
            ))}
          </select>
        </Field>
        <Field label="Description">
          <input
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
          />
        </Field>
        <label className="switch-field">
          <input
            type="checkbox"
            checked={form.active}
            onChange={(e) => setForm({ ...form, active: e.target.checked })}
          />
          <span />
          <div>
            <strong>Active train</strong>
            <small>Can be scheduled for service</small>
          </div>
        </label>
      </div>
      <div className="modal-actions">
        <button type="button" className="button secondary" onClick={onCancel}>
          Cancel
        </button>
        <button className="button primary">{initial ? 'Save changes' : 'Create train'}</button>
      </div>
    </form>
  )
}
