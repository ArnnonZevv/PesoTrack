import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

const STORAGE_KEY = 'pesotracker_token'
const USER_KEY = 'pesotracker_user'

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem(USER_KEY)
    return stored ? JSON.parse(stored) : null
  })

  function login(authResponse) {
    const { token, userId, username, fullname, role } = authResponse
    localStorage.setItem(STORAGE_KEY, token)
    const userInfo = { userId, username, fullname, role }
    localStorage.setItem(USER_KEY, JSON.stringify(userInfo))
    setUser(userInfo)
  }

  function logout() {
    localStorage.removeItem(STORAGE_KEY)
    localStorage.removeItem(USER_KEY)
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used inside an AuthProvider')
  return ctx
}
