import { request } from './api'
const path = '/api/admin/coaches'
export const coachApi = {
  list: () => request.get(path),
  listByTrain: (id) => request.get(`${path}/train/${id}`),
  get: (id) => request.get(`${path}/${id}`),
  create: (data) => request.post(path, data),
  update: (id, data) => request.put(`${path}/${id}`, data),
  remove: (id) => request.delete(`${path}/${id}`),
}
