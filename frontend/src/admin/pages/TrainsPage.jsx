import { useState } from 'react'
import { Armchair, ArrowRight, Edit3, Plus, TrainFront, Trash2 } from 'lucide-react'
import { Link } from 'react-router-dom'
import { trainApi } from '../services/trainApi'
import { routeApi } from '../services/routeApi'
import { useApi } from '../hooks/useApi'
import TrainForm from '../components/TrainForm'
import ConfirmDialog from '../components/ConfirmDialog'
import { Badge, Empty, ErrorBanner, Loading, Modal, PageHeader, TableShell } from '../components/Ui'
export default function TrainsPage() {
  const list = useApi(trainApi.list, []),
    routes = useApi(routeApi.list, [])
  const [editing, setEditing] = useState(null),
    [deleting, setDeleting] = useState(null),
    [error, setError] = useState('')
  const save = async (p) => {
    try {
      editing?.trainId ? await trainApi.update(editing.trainId, p) : await trainApi.create(p)
      setEditing(null)
      list.reload()
    } catch (e) {
      setError(e.message)
    }
  }
  const routeName = (id) =>
    routes.data.find((r) => Number(r.id) === Number(id))?.name || `Route #${id}`

  const openEdit = async (train) => {
    try {
      const details = await trainApi.get(train.trainId)
      setEditing({
        ...details,
        trainId: details.trainId ?? details.id ?? train.trainId,
        trainName: details.trainName ?? details.name ?? train.trainName,
        routId: details.routId ?? details.routeId ?? train.routId,
      })
    } catch (e) {
      setError(e.message)
    }
  }
  return (
    <>
      <PageHeader
        eyebrow="Fleet"
        title="Trains"
        description="Assign every train to a completed route, then configure coaches."
        actions={
          <button className="button primary" onClick={() => setEditing({})}>
            <Plus />
            Add train
          </button>
        }
      />
      <ErrorBanner message={list.error || error} />
      <section className="panel">
        <div className="list-toolbar">
          <div>
            <h2>Fleet directory</h2>
            <span>{list.data.length} trains</span>
          </div>
        </div>
        {list.loading ? (
          <Loading />
        ) : !list.data.length ? (
          <Empty
            title="No trains configured"
            text="Build a route before adding your first train."
          />
        ) : (
          <TableShell>
            <table>
              <thead>
                <tr>
                  <th>Train</th>
                  <th>Assigned route</th>
                  <th>Coaches</th>
                  <th>Configuration</th>
                  <th className="actions-cell">Actions</th>
                </tr>
              </thead>
              <tbody>
                {list.data.map((t) => (
                  <tr key={t.trainId}>
                    <td>
                      <div className="entity">
                        <span className="entity-icon green">
                          <TrainFront />
                        </span>
                        <div>
                          <strong>{t.trainName}</strong>
                          <small>Train no. {t.trainNumber}</small>
                        </div>
                      </div>
                    </td>
                    <td>{routeName(t.routId)}</td>
                    <td>
                      <strong>{t.totalCoachCount}</strong> total
                      <br />
                      <Badge tone="info">{t.reservedCoachCount} reserved</Badge>
                    </td>
                    <td>
                      <div className="action-links">
                        <Link
                          className="text-link"
                          to={`/admin/trains/${t.trainId}/coaches`}
                          state={{ train: t }}
                        >
                          <Armchair />
                          Coaches
                        </Link>
                        <Link
                          className="text-link"
                          to={`/admin/trains/${t.trainId}/seats`}
                          state={{ train: t }}
                        >
                          Seats <ArrowRight />
                        </Link>
                      </div>
                    </td>
                    <td className="actions-cell">
                      <button className="icon-button" onClick={() => openEdit(t)}>
                        <Edit3 />
                      </button>
                      <button className="icon-button danger-text" onClick={() => setDeleting(t)}>
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
          title={editing.trainId ? 'Edit train' : 'Add a train'}
          subtitle="A train inherits its station sequence from the selected route."
          onClose={() => setEditing(null)}
        >
          <ErrorBanner message={error} />
          <TrainForm
            initial={editing.trainId ? editing : null}
            routes={routes.data}
            onSubmit={save}
            onCancel={() => setEditing(null)}
          />
        </Modal>
      )}
      {deleting && (
        <ConfirmDialog
          message={`Delete ${deleting.trainName}? Existing schedules may prevent deletion.`}
          onCancel={() => setDeleting(null)}
          onConfirm={async () => {
            try {
              await trainApi.remove(deleting.trainId)
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
