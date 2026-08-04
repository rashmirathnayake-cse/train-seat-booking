import { request } from './api'

export const analyticsApi = {
  summary: () => request.get('/api/admin/analytics/summary'),
}
