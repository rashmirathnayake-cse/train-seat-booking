const BASE_URL = (import.meta.env.API_BASE_URL || '').replace(/\/$/, '')

export class ApiError extends Error {
  constructor(message, status) {
    super(message)
    this.status = status
  }
}

export async function api(path, options = {}) {
  const response = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers: {
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...options.headers,
    },
  })
  if (!response.ok) {
    let data
    try {
      data = await response.json()
    } catch {
      data = null
    }
    throw new ApiError(data?.message || `Request failed (${response.status})`, response.status)
  }
  if (response.status === 204) return null
  const responseText = await response.text()
  return responseText ? JSON.parse(responseText) : null
}

export const request = {
  get: (path) => api(path),
  post: (path, body) => api(path, { method: 'POST', body: JSON.stringify(body) }),
  put: (path, body) => api(path, { method: 'PUT', body: JSON.stringify(body) }),
  delete: (path) => api(path, { method: 'DELETE' }),
}
