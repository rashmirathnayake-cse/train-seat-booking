import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import AdminLayout from './admin/layout/AdminLayout'
import DashboardPage from './admin/pages/DashboardPage'
import StationsPage from './admin/pages/StationsPage'
import RoutesPage from './admin/pages/RoutesPage'
import RouteStationsPage from './admin/pages/RouteStationsPage'
import TrainsPage from './admin/pages/TrainsPage'
import CoachesPage from './admin/pages/CoachesPage'
import SeatsPage from './admin/pages/SeatsPage'
import TrainSchedulesPage from './admin/pages/TrainSchedulesPage'
import TimetablePage from './admin/pages/TimetablePage'
import BookingsPage from './admin/pages/BookingsPage'
import WelcomePage from './passenger/pages/WelcomePage'
import SeatSelectionPage from './passenger/pages/SeatSelectionPage'
import BookingConfirmationPage from './passenger/pages/BookingConfirmationPage'
import AnalyticsPage from './admin/pages/AnalyticsPage'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<WelcomePage />} />
        <Route path="/booking/seats" element={<SeatSelectionPage />} />
        <Route path="/booking/confirmation" element={<BookingConfirmationPage />} />
        <Route path="/admin" element={<AdminLayout />}>
          <Route index element={<DashboardPage />} />
          <Route path="stations" element={<StationsPage />} />
          <Route path="routes" element={<RoutesPage />} />
          <Route path="routes/:routeId/stations" element={<RouteStationsPage />} />
          <Route path="trains" element={<TrainsPage />} />
          <Route path="trains/:trainId/coaches" element={<CoachesPage />} />
          <Route path="trains/:trainId/seats" element={<SeatsPage />} />
          <Route path="schedules" element={<TrainSchedulesPage />} />
          <Route path="schedules/:scheduleId/timetable" element={<TimetablePage />} />
          <Route path="bookings" element={<BookingsPage />} />
          <Route path="analytics" element={<AnalyticsPage />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
