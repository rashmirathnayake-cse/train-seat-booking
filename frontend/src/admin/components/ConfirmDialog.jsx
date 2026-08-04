import { AlertTriangle } from 'lucide-react'
import { Modal } from './Ui'
export default function ConfirmDialog({
  title = 'Delete this record?',
  message,
  onCancel,
  onConfirm,
  busy,
}) {
  return (
    <Modal title={title} onClose={onCancel}>
      <div className="confirm-body">
        <div className="danger-icon">
          <AlertTriangle />
        </div>
        <p>
          {message ||
            'This action cannot be undone and may be rejected when related records exist.'}
        </p>
      </div>
      <div className="modal-actions">
        <button className="button secondary" onClick={onCancel}>
          Keep record
        </button>
        <button className="button danger" disabled={busy} onClick={onConfirm}>
          {busy ? 'Deleting…' : 'Delete'}
        </button>
      </div>
    </Modal>
  )
}
