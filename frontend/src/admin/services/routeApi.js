import { request } from './api'
const path = '/api/admin/routes'
export const routeApi = {
  list: () => request.get(path),
  get: (id) => request.get(`${path}/${id}`),
  create: (data) => request.post(path, data),
  update: (id, data) => request.put(`${path}/${id}`, data),
  remove: (id) => request.delete(`${path}/${id}`),
  listStations: (id) => request.get(`${path}/${id}/stations`),
  addStation: (id, data) => request.post(`${path}/${id}/stations`, data),
  updateStation: (id, data) => request.put(`${path}/stations/${id}`, data),
  removeStation: (id) => request.delete(`${path}/stations/${id}`),
}
