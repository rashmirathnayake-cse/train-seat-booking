import { Check, ChevronDown, MapPin, Search, X } from 'lucide-react'
import { useEffect, useMemo, useRef, useState } from 'react'

export default function StationSelect({ label, placeholder, stations, value, onChange, disabled }) {
  const [open, setOpen] = useState(false)
  const [query, setQuery] = useState('')
  const rootRef = useRef(null)
  const selected = stations.find((station) => String(station.id) === String(value))
  const filtered = useMemo(() => {
    const term = query.trim().toLowerCase()
    if (!term) return stations
    return stations.filter(
      (station) =>
        station.name.toLowerCase().includes(term) || station.code?.toLowerCase().includes(term),
    )
  }, [query, stations])

  useEffect(() => {
    const close = (event) => {
      if (!rootRef.current?.contains(event.target)) setOpen(false)
    }
    document.addEventListener('mousedown', close)
    return () => document.removeEventListener('mousedown', close)
  }, [])

  const choose = (station) => {
    onChange(station.id)
    setQuery('')
    setOpen(false)
  }

  return (
    <div className="station-select" ref={rootRef}>
      <label>{label}</label>
      <button
        type="button"
        className={`station-trigger ${open ? 'open' : ''}`}
        onClick={() => !disabled && setOpen((current) => !current)}
        disabled={disabled}
      >
        <span className="station-trigger-icon">
          <MapPin />
        </span>
        <span className="station-trigger-copy">
          {selected ? (
            <>
              <strong>{selected.name}</strong>
              <small>{selected.code}</small>
            </>
          ) : (
            <span>{disabled ? 'Loading stations…' : placeholder}</span>
          )}
        </span>
        {selected ? (
          <span
            className="station-clear"
            role="button"
            tabIndex="0"
            onClick={(event) => {
              event.stopPropagation()
              onChange('')
            }}
          >
            <X />
          </span>
        ) : (
          <ChevronDown className="station-chevron" />
        )}
      </button>
      {open && (
        <div className="station-menu">
          <div className="station-search">
            <Search />
            <input
              autoFocus
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Search by station or code"
            />
          </div>
          <div className="station-options">
            {filtered.length ? (
              filtered.map((station) => (
                <button type="button" key={station.id} onClick={() => choose(station)}>
                  <span className="option-marker" />
                  <span>
                    <strong>{station.name}</strong>
                    <small>{station.code}</small>
                  </span>
                  {String(station.id) === String(value) && <Check />}
                </button>
              ))
            ) : (
              <div className="no-stations">No matching stations found</div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}
