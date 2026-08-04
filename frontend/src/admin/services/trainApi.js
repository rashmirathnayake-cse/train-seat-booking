import { request } from './api'
const path = '/api/admin/trains'
export const trainApi = {
  list: () => request.get(path),
  get: (id) => request.get(`${path}/${id}`),
  create: (data) => request.post(path, data),
  update: (id, data) => request.put(`${path}/${id}`, data),
  remove: (id) => request.delete(`${path}/${id}`),
}
