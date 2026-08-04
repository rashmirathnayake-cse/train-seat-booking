import { request } from '../../admin/services/api'

export const passengerApi = {
  stations: () => request.get('/api/stations'),
  train: (trainId) => request.get(`/api/trains/${trainId}`),
  searchSchedules: ({ originStationId, destinationStationId, travelDate, departureAfter }) => {
    const params = new URLSearchParams({
      originStationId,
      destinationStationId,
      travelDate,
      departureAfter,
    })
    return request.get(`/api/train-schedules/search?${params.toString()}`)
  },
  seatMap: ({ scheduleId, originStopId, destinationStopId }) => {
    const params = new URLSearchParams({ originStopId, destinationStopId })
    return request.get(`/api/train-schedules/${scheduleId}/seat-map?${params.toString()}`)
  },
  createBooking: (booking) => request.post('/api/bookings', booking),
}
