import { useState } from 'react'
import { Field } from './Ui'
export default function RouteForm({ initial, onSubmit, onCancel }) {
  const [form, setForm] = useState({
    name: initial?.name || '',
    description: initial?.description || '',
    active: initial?.active ?? true,
  })
  const [busy, setBusy] = useState(false)
  const submit = async (e) => {
    e.preventDefault()
    setBusy(true)
    try {
      await onSubmit(form)
    } finally {
      setBusy(false)
    }
  }
  return (
    <form onSubmit={submit}>
      <div className="form-grid">
        <Field label="Route name">
          <input
            required
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            placeholder="Colombo Fort – Badulla"
          />
        </Field>
        <Field label="Description">
          <input
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            placeholder="Main Line"
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
            <strong>Active route</strong>
            <small>Available when assigning trains</small>
          </div>
        </label>
      </div>
      <div className="modal-actions">
        <button type="button" className="button secondary" onClick={onCancel}>
          Cancel
        </button>
        <button className="button primary" disabled={busy}>
          {busy ? 'Saving…' : initial ? 'Save changes' : 'Create route'}
        </button>
      </div>
    </form>
  )
}
