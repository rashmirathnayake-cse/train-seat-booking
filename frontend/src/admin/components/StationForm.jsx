import { useState } from 'react'
import { Field } from './Ui'
export default function StationForm({ initial, onSubmit, onCancel }) {
  const [form, setForm] = useState({
    name: initial?.name || '',
    code: initial?.code || '',
    sequenceNumber: initial?.sequenceNumber || '',
  })
  const [busy, setBusy] = useState(false)
  const submit = async (e) => {
    e.preventDefault()
    setBusy(true)
    try {
      await onSubmit({ ...form, sequenceNumber: Number(form.sequenceNumber) })
    } finally {
      setBusy(false)
    }
  }
  return (
    <form onSubmit={submit}>
      <div className="form-grid">
        <Field label="Station name">
          <input
            required
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            placeholder="e.g. Kandy"
          />
        </Field>
        <Field label="Station code">
          <input
            required
            maxLength="5"
            value={form.code}
            onChange={(e) => setForm({ ...form, code: e.target.value.toUpperCase() })}
            placeholder="KDY"
          />
        </Field>
        <Field label="Network sequence">
          <input
            required
            min="1"
            type="number"
            value={form.sequenceNumber}
            onChange={(e) => setForm({ ...form, sequenceNumber: e.target.value })}
          />
        </Field>
      </div>
      <div className="modal-actions">
        <button type="button" className="button secondary" onClick={onCancel}>
          Cancel
        </button>
        <button className="button primary" disabled={busy}>
          {busy ? 'Saving…' : initial ? 'Save changes' : 'Create station'}
        </button>
      </div>
    </form>
  )
}
