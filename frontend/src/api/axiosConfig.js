import axios from 'axios'

/**
 * Shared axios instance so the backend base URL & headers are configured once.
 * withCredentials matches the backend's allowCredentials(true) CORS setting.
 */
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

export default api
