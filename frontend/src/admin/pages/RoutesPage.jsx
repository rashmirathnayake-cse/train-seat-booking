import { useState } from 'react'
import { ArrowRight, Edit3, Map, Plus, Trash2 } from 'lucide-react'
import { Link } from 'react-router-dom'
import { routeApi } from '../services/routeApi'
import { useApi } from '../hooks/useApi'
import RouteForm from '../components/RouteForm'
import ConfirmDialog from '../components/ConfirmDialog'
import { Badge, Empty, ErrorBanner, Loading, Modal, PageHeader, TableShell } from '../components/Ui'
export default function RoutesPage() {
  const { data, loading, error, reload } = useApi(routeApi.list, [])
  const [editing, setEditing] = useState(null)
  const [deleting, setDeleting] = useState(null)
  const [localError, setLocalError] = useState('')
  const save = async (p) => {
    try {
      editing?.id ? await routeApi.update(editing.id, p) : await routeApi.create(p)
      setEditing(null)
      reload()
    } catch (e) {
      setLocalError(e.message)
    }
  }
  return (
    <>
      <PageHeader
        eyebrow="Network"
        title="Routes"
        description="Create routes, then arrange their stations in travel order."
        actions={
          <button className="button primary" onClick={() => setEditing({})}>
            <Plus />
            Create route
          </button>
        }
      />
      <ErrorBanner message={error || localError} />
      <section className="panel">
        <div className="list-toolbar">
          <div>
            <h2>Route directory</h2>
            <span>{data.length} routes</span>
          </div>
        </div>
        {loading ? (
          <Loading />
        ) : !data.length ? (
          <Empty title="No routes configured" text="Create a route after adding your stations." />
        ) : (
          <TableShell>
            <table>
              <thead>
                <tr>
                  <th>Route</th>
                  <th>Description</th>
                  <th>Status</th>
                  <th>Route setup</th>
                  <th className="actions-cell">Actions</th>
                </tr>
              </thead>
              <tbody>
                {data.map((r) => (
                  <tr key={r.id}>
                    <td>
                      <div className="entity">
                        <span className="entity-icon purple">
                          <Map />
                        </span>
                        <div>
                          <strong>{r.name}</strong>
                          <small>Route ID #{r.id}</small>
                        </div>
                      </div>
                    </td>
                    <td>{r.description || '—'}</td>
                    <td>
                      <Badge tone={r.active ? 'success' : 'neutral'}>
                        {r.active ? 'Active' : 'Inactive'}
                      </Badge>
                    </td>
                    <td>
                      <Link
                        className="text-link"
                        to={`/admin/routes/${r.id}/stations`}
                        state={{ route: r }}
                      >
                        Manage stations <ArrowRight />
                      </Link>
                    </td>
                    <td className="actions-cell">
                      <button className="icon-button" onClick={() => setEditing(r)}>
                        <Edit3 />
                      </button>
                      <button className="icon-button danger-text" onClick={() => setDeleting(r)}>
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
          title={editing.id ? 'Edit route' : 'Create a route'}
          subtitle="Route stations are configured after this step."
          onClose={() => setEditing(null)}
        >
          <ErrorBanner message={localError} />
          <RouteForm
            initial={editing.id ? editing : null}
            onSubmit={save}
            onCancel={() => setEditing(null)}
          />
        </Modal>
      )}
      {deleting && (
        <ConfirmDialog
          message={`Delete ${deleting.name}? Trains assigned to it may prevent deletion.`}
          onCancel={() => setDeleting(null)}
          onConfirm={async () => {
            try {
              await routeApi.remove(deleting.id)
              setDeleting(null)
              reload()
            } catch (e) {
              setLocalError(e.message)
              setDeleting(null)
            }
          }}
        />
      )}
    </>
  )
}
