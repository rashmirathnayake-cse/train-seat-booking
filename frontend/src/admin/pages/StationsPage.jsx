import { useState } from 'react'
import { Edit3, MapPin, Plus, Trash2 } from 'lucide-react'
import { stationApi } from '../services/stationApi'
import { useApi } from '../hooks/useApi'
import StationForm from '../components/StationForm'
import ConfirmDialog from '../components/ConfirmDialog'
import { Empty, ErrorBanner, Loading, Modal, PageHeader, TableShell } from '../components/Ui'
export default function StationsPage() {
  const { data, loading, error, reload } = useApi(stationApi.list, [])
  const [editing, setEditing] = useState(null)
  const [deleting, setDeleting] = useState(null)
  const [formError, setFormError] = useState('')
  const save = async (payload) => {
    try {
      editing?.id ? await stationApi.update(editing.id, payload) : await stationApi.create(payload)
      setEditing(null)
      reload()
    } catch (e) {
      setFormError(e.message)
    }
  }
  return (
    <>
      <PageHeader
        eyebrow="Network"
        title="Stations"
        description="Create the master list of stations before building routes."
        actions={
          <button className="button primary" onClick={() => setEditing({})}>
            <Plus size={17} />
            Add station
          </button>
        }
      />
      <ErrorBanner message={error} />
      <section className="panel">
        <div className="list-toolbar">
          <div>
            <h2>Station directory</h2>
            <span>{data.length} stations</span>
          </div>
        </div>
        {loading ? (
          <Loading />
        ) : data.length === 0 ? (
          <Empty title="No stations configured" />
        ) : (
          <TableShell>
            <table>
              <thead>
                <tr>
                  <th>Station</th>
                  <th>Code</th>
                  <th>Network sequence</th>
                  <th className="actions-cell">Actions</th>
                </tr>
              </thead>
              <tbody>
                {data.map((s) => (
                  <tr key={s.id}>
                    <td>
                      <div className="entity">
                        <span className="entity-icon">
                          <MapPin />
                        </span>
                        <div>
                          <strong>{s.name}</strong>
                          <small>Station ID #{s.id}</small>
                        </div>
                      </div>
                    </td>
                    <td>
                      <code className="code-chip">{s.code}</code>
                    </td>
                    <td>{s.sequenceNumber ?? '—'}</td>
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
          title={editing.id ? 'Edit station' : 'Add a station'}
          subtitle="Station codes should be short and unique."
          onClose={() => setEditing(null)}
        >
          <ErrorBanner message={formError} />
          <StationForm
            initial={editing.id ? editing : null}
            onSubmit={save}
            onCancel={() => setEditing(null)}
          />
        </Modal>
      )}
      {deleting && (
        <ConfirmDialog
          message={`Delete ${deleting.name}? Routes using this station will prevent deletion.`}
          onCancel={() => setDeleting(null)}
          onConfirm={async () => {
            try {
              await stationApi.remove(deleting.id)
              setDeleting(null)
              reload()
            } catch (e) {
              setFormError(e.message)
              setDeleting(null)
            }
          }}
        />
      )}
    </>
  )
}
