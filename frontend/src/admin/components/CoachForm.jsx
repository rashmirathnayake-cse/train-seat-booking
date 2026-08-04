import { useState } from 'react'
import { Field } from './Ui'
export default function CoachForm({ initial, trainId, onSubmit, onCancel }) {
  const [form, setForm] = useState({
    coachNumber: initial?.coachNumber || '',
    type: initial?.type || 'RESERVED',
    seatCapacity: initial?.seatCapacity || 48,
  })
  const submit = (e) => {
    e.preventDefault()
    onSubmit(
      initial
        ? { coachNumber: form.coachNumber, type: form.type }
        : { ...form, seatCapacity: Number(form.seatCapacity), trainId: Number(trainId) },
    )
  }
  return (
    <form onSubmit={submit}>
      <div className="form-grid">
        <Field label="Coach number">
          <input
            required
            value={form.coachNumber}
            onChange={(e) => setForm({ ...form, coachNumber: e.target.value.toUpperCase() })}
            placeholder="R1"
          />
        </Field>
        <Field label="Coach type">
          <select value={form.type} onChange={(e) => setForm({ ...form, type: e.target.value })}>
            <option value="RESERVED">Reserved</option>
            <option value="UNRESERVED">Unreserved</option>
          </select>
        </Field>
        {!initial && (
          <Field label="Seat capacity" hint="Seats 1 through capacity are generated automatically.">
            <input
              required
              min="1"
              max="200"
              type="number"
              value={form.seatCapacity}
              onChange={(e) => setForm({ ...form, seatCapacity: e.target.value })}
            />
          </Field>
        )}
      </div>
      <div className="modal-actions">
        <button type="button" className="button secondary" onClick={onCancel}>
          Cancel
        </button>
        <button className="button primary">
          {initial ? 'Save changes' : 'Add coach & generate seats'}
        </button>
      </div>
    </form>
  )
}
