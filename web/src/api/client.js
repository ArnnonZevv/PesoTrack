import axios from 'axios'

const client = axios.create({
  baseURL: '/api',
})

// Attach the stored JWT (if any) to every outgoing request.
client.interceptors.request.use((config) => {
  const token = localStorage.getItem('pesotracker_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default client
