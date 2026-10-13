import axios from 'axios'
// All server calls go through this client. Base URL comes from VITE_API_URL (see .env.example).
export const api = axios.create({ baseURL: (import.meta.env.VITE_API_URL ?? 'http://localhost:8081') + '/api' })
api.interceptors.request.use(c => { const t = localStorage.getItem('token'); if (t) c.headers.Authorization = `Bearer ${t}`; return c })
api.interceptors.response.use(r => r, e => {
  if (e.response?.status === 401 && !String(e.config?.url).startsWith('/auth')) { localStorage.removeItem('token'); window.location.href = '/login' }
  return Promise.reject(e) })
export const errMsg = (e: any): string => e?.response?.data?.message ?? 'Something went wrong. Please try again.'
export const fmt = (d: string) => new Date(d).toLocaleString([], { dateStyle: 'medium', timeStyle: 'short' })
