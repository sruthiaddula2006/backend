import { createContext, useContext, useEffect, useState, ReactNode } from 'react'
import { api } from './api'
import { User } from './types'
interface Ctx { user:User|null; loading:boolean; login:(email:string,password:string)=>Promise<void>; register:(f:Record<string,string>)=>Promise<void>; logout:()=>void; setUser:(u:User)=>void }
const AuthCtx = createContext<Ctx>(null as unknown as Ctx)
export const useAuth = () => useContext(AuthCtx)
export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User|null>(null), [loading, setLoading] = useState(!!localStorage.getItem('token'))
  useEffect(() => { if (localStorage.getItem('token')) api.get<User>('/users/me').then(r => setUser(r.data)).catch(() => localStorage.removeItem('token')).finally(() => setLoading(false)) }, [])
  const store = (d:{token:string;user:User}) => { localStorage.setItem('token', d.token); setUser(d.user) }
  return <AuthCtx.Provider value={{ user, loading, setUser,
    login: async (email, password) => store((await api.post('/auth/login', { email, password })).data),
    register: async f => store((await api.post('/auth/register', f)).data),
    logout: () => { localStorage.removeItem('token'); setUser(null) } }}>{children}</AuthCtx.Provider>
}
