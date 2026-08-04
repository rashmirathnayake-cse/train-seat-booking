import { Phone, UserRound } from 'lucide-react'
import { useState } from 'react'

export default function PassengerDetailsForm({ onSubmit, busy }) {
  const [passengerName, setPassengerName] = useState('')
  const [phone, setPhone] = useState('')
  const submit = (event) => {
    event.preventDefault()
    onSubmit({ passengerName: passengerName.trim(), phone: phone.trim() })
  }
  return (
    <form className="passenger-details-form" onSubmit={submit}>
      <div className="booking-section-heading">
        <span>2</span>
        <div>
          <h2>Passenger details</h2>
          <p>Enter the details for this reservation.</p>
        </div>
      </div>
      <label>
        <span>Full name</span>
        <div>
          <UserRound />
          <input
            required
            minLength="2"
            value={passengerName}
            onChange={(event) => setPassengerName(event.target.value)}
            placeholder="Passenger full name"
          />
        </div>
      </label>
      <label>
        <span>Phone number</span>
        <div>
          <Phone />
          <input
            required
            inputMode="tel"
            pattern="[0-9+ ]{9,15}"
            value={phone}
            onChange={(event) => setPhone(event.target.value)}
            placeholder="077 123 4567"
          />
        </div>
      </label>
      <button className="confirm-booking-button" disabled={busy}>
        {busy ? 'Confirming your seat…' : 'Confirm booking'}
      </button>
      <small className="booking-disclaimer">Availability is checked again when you confirm.</small>
    </form>
  )
}
