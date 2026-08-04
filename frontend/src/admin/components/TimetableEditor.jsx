import { Clock3 } from 'lucide-react'
export default function TimetableEditor({ stops, onChange }) {
  const update = (id, key, value) =>
    onChange(stops.map((s) => (s.id === id ? { ...s, [key]: value } : s)))
  return (
    <div className="timetable">
      <div className="timetable-head">
        <span>Stop</span>
        <span>Station</span>
        <span>Arrival</span>
        <span>Departure</span>
      </div>
      {stops.map((s, i) => (
        <div className="timetable-row" key={s.id}>
          <div className="time-route">
            <span>{s.stopOrder}</span>
            {i < stops.length - 1 && <i />}
          </div>
          <div>
            <strong>{s.stationName}</strong>
            <small>{s.stationCode || `${s.distanceFromOrigin ?? 0} km`}</small>
          </div>
          <label className="time-input">
            <Clock3 />
            <input
              aria-label={`${s.stationName} arrival`}
              type="time"
              value={(s.arrivalTime || '').slice(0, 5)}
              disabled={i === 0}
              onChange={(e) => update(s.id, 'arrivalTime', e.target.value || null)}
            />
          </label>
          <label className="time-input">
            <Clock3 />
            <input
              aria-label={`${s.stationName} departure`}
              type="time"
              value={(s.departureTime || '').slice(0, 5)}
              disabled={i === stops.length - 1}
              onChange={(e) => update(s.id, 'departureTime', e.target.value || null)}
            />
          </label>
        </div>
      ))}
    </div>
  )
}
