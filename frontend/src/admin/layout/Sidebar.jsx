import {
  Armchair,
  BarChart3,
  CalendarDays,
  LayoutDashboard,
  Map,
  MapPin,
  TicketCheck,
  TrainFront,
  X,
} from 'lucide-react'
import { createElement } from 'react'
import { NavLink } from 'react-router-dom'

const links = [
  { to: '/admin', label: 'Overview', icon: LayoutDashboard, end: true },
  { to: '/admin/stations', label: 'Stations', icon: MapPin },
  { to: '/admin/routes', label: 'Routes', icon: Map },
  { to: '/admin/trains', label: 'Trains', icon: TrainFront },
  { to: '/admin/schedules', label: 'Schedules', icon: CalendarDays },
  { to: '/admin/bookings', label: 'Bookings', icon: TicketCheck },
  { to: '/admin/analytics', label: 'Analytics', icon: BarChart3 },
]
export default function Sidebar({ open, onClose }) {
  return (
    <aside className={`sidebar ${open ? 'open' : ''}`}>
      <div className="brand">
        <div className="brand-mark">
          <Armchair size={21} />
        </div>
        <div>
          <strong>RailFlow</strong>
          <span>Operations</span>
        </div>
        <button className="sidebar-close" onClick={onClose}>
          <X />
        </button>
      </div>
      <nav>
        <p>Workspace</p>
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            end={link.end}
            onClick={onClose}
            className={({ isActive }) => (isActive ? 'active' : '')}
          >
            {createElement(link.icon, { size: 19 })}
            <span>{link.label}</span>
          </NavLink>
        ))}
      </nav>
      <div className="sidebar-foot">
        <span>API environment</span>
        <strong>
          <i /> Connected backend
        </strong>
        <small>Proxied through RailFlow</small>
      </div>
    </aside>
  )
}
