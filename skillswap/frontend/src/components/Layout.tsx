import { useEffect, useState } from 'react'
import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useAuth } from '../AuthContext'
import { api } from '../api'
import { Notif } from '../types'
const links: [string,string][] = [['/dashboard','Dashboard'],['/matches','Find Matches'],['/requests','Requests'],['/exchanges','Exchanges'],['/messages','Messages'],['/profile','Profile']]
export default function Layout() {
  const { user, logout } = useAuth(), nav = useNavigate()
  const [dark, setDark] = useState(localStorage.getItem('theme') === 'dark'), [unread, setUnread] = useState(0)
  useEffect(() => { document.documentElement.dataset.theme = dark ? 'dark' : 'light'; localStorage.setItem('theme', dark ? 'dark' : 'light') }, [dark])
  useEffect(() => { if (!user) return; const load = () => api.get<Notif[]>('/notifications').then(r => setUnread(r.data.filter(n => !n.read).length)).catch(() => {}); load(); const t = setInterval(load, 15000); return () => clearInterval(t) }, [user])
  return <><div className="nav"><Link to={user ? '/dashboard' : '/'} className="logo">🔄 SkillSwap</Link>
    <div className="links">{user ? links.map(([to, t]) => <NavLink key={to} to={to} className={({ isActive }) => isActive ? 'on' : ''}>{t}</NavLink>) : <NavLink to="/">Home</NavLink>}</div>
    <button className="btn g sm" onClick={() => setDark(!dark)}>{dark ? '☀️' : '🌙'}</button>
    {user ? <><button className="btn g sm" onClick={() => nav('/notifications')}>🔔{unread > 0 && <span className="badge">{unread}</span>}</button><button className="btn sm" onClick={() => { logout(); nav('/') }}>Logout</button></>
      : <><button className="btn g sm" onClick={() => nav('/login')}>Login</button><button className="btn sm" onClick={() => nav('/register')}>Register</button></>}</div>
    <Outlet /></>
}
