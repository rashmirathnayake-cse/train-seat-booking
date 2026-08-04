import { useState } from 'react'
import { ArrowLeft, Armchair, Edit3, Plus, Trash2 } from 'lucide-react'
import { Link, useLocation, useParams } from 'react-router-dom'
import { coachApi } from '../services/coachApi'
import { useApi } from '../hooks/useApi'
import CoachForm from '../components/CoachForm'
import ConfirmDialog from '../components/ConfirmDialog'
import { Badge, Empty, ErrorBanner, Loading, Modal, PageHeader, TableShell } from '../components/Ui'
export default function CoachesPage() {
  const { trainId } = useParams()
  const train = useLocation().state?.train
  const list = useApi(() => coachApi.listByTrain(trainId), [trainId])
  const [editing, setEditing] = useState(null),
    [deleting, setDeleting] = useState(null),
    [error, setError] = useState('')
  const save = async (p) => {
    try {
      editing?.id ? await coachApi.update(editing.id, p) : await coachApi.create(p)
      setEditing(null)
      list.reload()
    } catch (e) {
      setError(e.message)
    }
  }
  return (
    <>
      <Link className="back-link" to="/admin/trains">
        <ArrowLeft />
        Back to trains
      </Link>
      <PageHeader
        eyebrow="Fleet configuration"
        title={`${train?.trainName || train?.name || `Train #${trainId}`} coaches`}
        description="Adding a coach automatically generates its numbered seats."
        actions={
          <button className="button primary" onClick={() => setEditing({})}>
            <Plus />
            Add coach
          </button>
        }
      />
      <ErrorBanner message={list.error || error} />
      <section className="panel">
        <div className="list-toolbar">
          <div>
            <h2>Coach formation</h2>
            <span>{list.data.reduce((n, c) => n + (c.seatCapacity || 0), 0)} total seats</span>
          </div>
          <Link
            className="button secondary"
            to={`/admin/trains/${trainId}/seats`}
            state={{ train }}
          >
            View seats
          </Link>
        </div>
        {list.loading ? (
          <Loading />
        ) : !list.data.length ? (
          <Empty title="No coaches added" text="Add a reserved coach to generate bookable seats." />
        ) : (
          <TableShell>
            <table>
              <thead>
                <tr>
                  <th>Coach</th>
                  <th>Type</th>
                  <th>Capacity</th>
                  <th>Generated seats</th>
                  <th className="actions-cell">Actions</th>
                </tr>
              </thead>
              <tbody>
                {list.data.map((c) => (
                  <tr key={c.id}>
                    <td>
                      <div className="entity">
                        <span className="entity-icon amber">
                          <Armchair />
                        </span>
                        <div>
                          <strong>Coach {c.coachNumber}</strong>
                          <small>Coach ID #{c.id}</small>
                        </div>
                      </div>
                    </td>
                    <td>
                      <Badge tone={c.type === 'RESERVED' ? 'info' : 'neutral'}>{c.type}</Badge>
                    </td>
                    <td>{c.seatCapacity}</td>
                    <td>
                      <Badge tone="success">{c.seatCapacity} generated</Badge>
                    </td>
                    <td className="actions-cell">
                      <button className="icon-button" onClick={() => setEditing(c)}>
                        <Edit3 />
                      </button>
                      <button className="icon-button danger-text" onClick={() => setDeleting(c)}>
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
          title={editing.id ? 'Edit coach' : 'Add a coach'}
          subtitle={!editing.id ? 'Seat records will be generated automatically.' : ''}
          onClose={() => setEditing(null)}
        >
          <ErrorBanner message={error} />
          <CoachForm
            initial={editing.id ? editing : null}
            trainId={trainId}
            onSubmit={save}
            onCancel={() => setEditing(null)}
          />
        </Modal>
      )}
      {deleting && (
        <ConfirmDialog
          message={`Delete coach ${deleting.coachNumber} and its generated seats?`}
          onCancel={() => setDeleting(null)}
          onConfirm={async () => {
            try {
              await coachApi.remove(deleting.id)
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
