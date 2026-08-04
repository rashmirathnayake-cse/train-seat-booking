import { AlertCircle, Inbox, LoaderCircle, X } from 'lucide-react'

export function PageHeader({ eyebrow, title, description, actions }) {
  return (
    <div className="page-header">
      <div>
        <span className="eyebrow">{eyebrow}</span>
        <h1>{title}</h1>
        {description && <p>{description}</p>}
      </div>
      {actions && <div className="header-actions">{actions}</div>}
    </div>
  )
}
export function Badge({ children, tone = 'neutral' }) {
  return <span className={`badge badge-${tone}`}>{children}</span>
}
export function Loading() {
  return (
    <div className="state">
      <LoaderCircle className="spin" />
      <span>Loading records…</span>
    </div>
  )
}
export function Empty({
  title = 'No records yet',
  text = 'Create the first record to get started.',
}) {
  return (
    <div className="state empty">
      <Inbox />
      <strong>{title}</strong>
      <span>{text}</span>
    </div>
  )
}
export function ErrorBanner({ message }) {
  return message ? (
    <div className="error-banner">
      <AlertCircle size={18} />
      <span>{message}</span>
    </div>
  ) : null
}
export function Modal({ title, subtitle, onClose, children }) {
  return (
    <div className="modal-backdrop" onMouseDown={(e) => e.target === e.currentTarget && onClose()}>
      <div className="modal">
        <div className="modal-head">
          <div>
            <h2>{title}</h2>
            {subtitle && <p>{subtitle}</p>}
          </div>
          <button className="icon-button" onClick={onClose} aria-label="Close">
            <X size={20} />
          </button>
        </div>
        {children}
      </div>
    </div>
  )
}
export function Field({ label, children, hint }) {
  return (
    <label className="field">
      <span>{label}</span>
      {children}
      {hint && <small>{hint}</small>}
    </label>
  )
}
export function TableShell({ children }) {
  return <div className="table-shell">{children}</div>
}
