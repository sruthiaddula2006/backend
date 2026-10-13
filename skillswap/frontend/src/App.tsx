import { Routes, Route, Navigate, Outlet } from 'react-router-dom'
import { useAuth } from './AuthContext'
import Layout from './components/Layout'
import Landing from './pages/Landing'
import { Login, Register } from './pages/Auth'
import Dashboard from './pages/Dashboard'
import Matches from './pages/Matches'
import UserProfile from './pages/UserProfile'
import Profile from './pages/Profile'
import Requests from './pages/Requests'
import Exchanges from './pages/Exchanges'
import Messages from './pages/Messages'
import Notifications from './pages/Notifications'
function Protected() { const { user, loading } = useAuth(); if (loading) return <div className="empty">Loading…</div>; return user ? <Outlet /> : <Navigate to="/login" replace /> }
export default function App() {
  return <Routes><Route element={<Layout />}>
    <Route path="/" element={<Landing />} /><Route path="/login" element={<Login />} /><Route path="/register" element={<Register />} />
    <Route element={<Protected />}>
      <Route path="/dashboard" element={<Dashboard />} /><Route path="/matches" element={<Matches />} /><Route path="/users/:id" element={<UserProfile />} />
      <Route path="/profile" element={<Profile />} /><Route path="/requests" element={<Requests />} /><Route path="/exchanges" element={<Exchanges />} />
      <Route path="/messages" element={<Messages />} /><Route path="/notifications" element={<Notifications />} />
    </Route><Route path="*" element={<Navigate to="/" />} /></Route></Routes>
}
