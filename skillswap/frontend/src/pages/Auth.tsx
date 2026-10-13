import { useState, FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../AuthContext'
import { errMsg } from '../api'
export function Login() {
  const { login } = useAuth(), nav = useNavigate(), [f, setF] = useState({ email: '', password: '' }), [err, setErr] = useState('')
  const submit = async (e: FormEvent) => { e.preventDefault(); try { await login(f.email, f.password); nav('/dashboard') } catch (x) { setErr(errMsg(x)) } }
  return <div className="card auth"><h2>Welcome back</h2><form onSubmit={submit}>
    <input placeholder="Email" value={f.email} onChange={e => setF({ ...f, email: e.target.value })} /><input type="password" placeholder="Password" value={f.password} onChange={e => setF({ ...f, password: e.target.value })} />
    {err && <div className="err">{err}</div>}<button className="btn" style={{ width: '100%' }}>Login</button></form>
    <p className="mu">Demo (when SEED_DEMO=true): demo@skillswap.com / demo123</p><p className="mu">No account? <Link to="/register">Register</Link></p></div>
}
export function Register() {
  const { register } = useAuth(), nav = useNavigate(), [f, setF] = useState<Record<string,string>>({ name: '', email: '', password: '', college: '', location: '', bio: '' }), [err, setErr] = useState('')
  const set = (k: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => setF({ ...f, [k]: e.target.value })
  const submit = async (e: FormEvent) => { e.preventDefault(); try { await register(f); nav('/profile') } catch (x) { setErr(errMsg(x)) } }
  return <div className="card auth"><h2>Create account</h2><form onSubmit={submit}>
    <input placeholder="Full Name" value={f.name} onChange={set('name')} /><input placeholder="Email" value={f.email} onChange={set('email')} /><input type="password" placeholder="Password (min 6 chars)" value={f.password} onChange={set('password')} />
    <input placeholder="College / Organization" value={f.college} onChange={set('college')} /><input placeholder="Location" value={f.location} onChange={set('location')} /><textarea placeholder="Short bio" value={f.bio} onChange={set('bio')} />
    {err && <div className="err">{err}</div>}<button className="btn" style={{ width: '100%' }}>Register</button></form>
    <p className="mu">Already registered? <Link to="/login">Login</Link></p></div>
}
