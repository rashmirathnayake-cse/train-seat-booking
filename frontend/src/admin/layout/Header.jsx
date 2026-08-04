import { Menu, Search } from 'lucide-react'
export default function Header({ onMenu }) {
  return (
    <header className="topbar">
      <button className="menu-button" onClick={onMenu}>
        <Menu />
      </button>
      <div className="top-search">
        <Search size={18} />
        <input placeholder="Search operations…" />
      </div>
      <div className="top-actions">
        <div className="avatar">AD</div>
        <div className="admin-name">
          <strong>Admin User</strong>
          <span>System administrator</span>
        </div>
      </div>
    </header>
  )
}
