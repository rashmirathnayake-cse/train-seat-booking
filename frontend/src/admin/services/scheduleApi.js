import { request } from './api'
const path = '/api/admin/train-schedules'
export const scheduleApi = {
  list: () => request.get(path),
  get: (id) => request.get(`${path}/${id}`),
  create: (data) => request.post(path, data),
  update: (id, data) => request.put(`${path}/${id}`, data),
  remove: (id) => request.delete(`${path}/${id}`),
  stops: (id) => request.get(`${path}/${id}/stops`),
  updateAllStops: (scheduleId, scheduleStops) =>
    request.put(`${path}/${scheduleId}/stops`, scheduleStops),
  updateStop: (id, data) => request.put(`/api/admin/schedule-stops/${id}`, data),
}
