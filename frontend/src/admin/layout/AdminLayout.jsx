import { useState } from 'react'
import { Outlet } from 'react-router-dom'
import Header from './Header'
import Sidebar from './Sidebar'
export default function AdminLayout() {
  const [open, setOpen] = useState(false)
  return (
    <div className="admin-shell">
      <Sidebar open={open} onClose={() => setOpen(false)} />
      <div className="main-area">
        <Header onMenu={() => setOpen(true)} />
        <main>
          <Outlet />
        </main>
      </div>
      {open && <div className="mobile-overlay" onClick={() => setOpen(false)} />}
    </div>
  )
}
