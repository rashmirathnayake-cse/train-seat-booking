import { useEffect, useState } from 'react'
import { ArrowLeft, CheckCircle2, Save, Send } from 'lucide-react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { scheduleApi } from '../services/scheduleApi'
import TimetableEditor from '../components/TimetableEditor'
import { Badge, ErrorBanner, Loading, PageHeader } from '../components/Ui'
export default function TimetablePage() {
  const { scheduleId } = useParams()
  const initial = useLocation().state?.schedule
  const [stops, setStops] = useState([]),
    [schedule, setSchedule] = useState(initial),
    [loading, setLoading] = useState(true),
    [error, setError] = useState(''),
    [saved, setSaved] = useState(false)
  useEffect(() => {
    Promise.all([
      scheduleApi.stops(scheduleId),
      initial ? Promise.resolve(initial) : scheduleApi.get(scheduleId),
    ])
      .then(([a, b]) => {
        setStops(a)
        setSchedule(b)
      })
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false))
  }, [scheduleId, initial])
  const validate = () => {
    for (let i = 0; i < stops.length; i++) {
      const s = stops[i]
      if (i > 0 && !s.arrivalTime) return `Arrival time is missing for ${s.stationName}.`
      if (i < stops.length - 1 && !s.departureTime)
        return `Departure time is missing for ${s.stationName}.`
      if (s.arrivalTime && s.departureTime && s.departureTime < s.arrivalTime)
        return `Departure is before arrival at ${s.stationName}.`
    }
    return ''
  }
  const save = async () => {
    const issue = validate()
    if (issue) {
      setError(issue)
      return false
    }
    try {
      const scheduleStopUpdates = stops.map((stop) => ({
        id: stop.id,
        arrivalTime: stop.arrivalTime ? stop.arrivalTime.slice(0, 5) : null,
        departureTime: stop.departureTime ? stop.departureTime.slice(0, 5) : null,
      }))

      console.group(`Timetable update request — schedule ${scheduleId}`)
      console.log('Endpoint:', `/api/admin/train-schedules/${scheduleId}/stops`)
      console.log('Schedule ID:', Number(scheduleId))
      console.log('Request body:', scheduleStopUpdates)
      console.table(scheduleStopUpdates)
      console.groupEnd()

      await scheduleApi.updateAllStops(Number(scheduleId), scheduleStopUpdates)

      // The bulk update may return a list or an empty 204 response depending
      // on the backend controller. Reload the authoritative timetable either way.
      const refreshedStops = await scheduleApi.stops(Number(scheduleId))
      setStops(refreshedStops)
      setSaved(true)
      setError('')
      return true
    } catch (e) {
      setError(e.message)
      return false
    }
  }
  const publish = async () => {
    if (!(await save())) return
    try {
      const next = await scheduleApi.update(scheduleId, {
        trainId: schedule.trainId,
        travelDate: schedule.travelDate,
        status: 'ACTIVE',
      })
      setSchedule(next)
    } catch (e) {
      setError(e.message)
    }
  }
  return (
    <>
      <Link className="back-link" to="/admin/schedules">
        <ArrowLeft />
        Back to schedules
      </Link>
      <PageHeader
        eyebrow="Timetable editor"
        title={`Schedule #${scheduleId}`}
        description={`${schedule?.trainNumber ? `Train ${schedule.trainNumber} · ` : ''}${schedule?.travelDate || 'Dated service'}`}
        actions={
          <>
            <Badge tone={schedule?.status === 'ACTIVE' ? 'success' : 'warning'}>
              {schedule?.status || 'DRAFT'}
            </Badge>
            <button className="button secondary" onClick={save}>
              <Save />
              Save draft
            </button>
            <button
              className="button primary"
              onClick={publish}
              disabled={schedule?.status === 'ACTIVE'}
            >
              <Send />
              {schedule?.status === 'ACTIVE' ? 'Published' : 'Validate & publish'}
            </button>
          </>
        }
      />
      <ErrorBanner message={error} />
      {saved && !error && (
        <div className="success-banner">
          <CheckCircle2 />
          Timetable saved and validated successfully.
        </div>
      )}
      {loading ? (
        <Loading />
      ) : (
        <section className="panel timetable-panel">
          <div className="list-toolbar">
            <div>
              <h2>Stop times</h2>
              <span>Times follow the service travel date</span>
            </div>
            <div className="timetable-hint">
              First stop has departure only · Last stop has arrival only
            </div>
          </div>
          <TimetableEditor
            stops={stops}
            onChange={(v) => {
              setStops(v)
              setSaved(false)
            }}
          />
        </section>
      )}
    </>
  )
}
