import { useState } from 'react'
import { api, errMsg } from '../api'
import { useAuth } from '../AuthContext'
import { AVATARS } from '../skills'
import { User } from '../types'
import { Avatar, Chips, SkillPicker } from '../components/Ui'
type K = 'skillsKnown' | 'skillsWanted'
export default function Profile() {
  const { user, setUser } = useAuth(), [d, setD] = useState<User>(user!), [err, setErr] = useState(''), [ok, setOk] = useState(false)
  const add = (k: K) => (s: string) => { if (d[k].some(x => x.toLowerCase() === s.toLowerCase())) { setErr('You already have this skill.'); return 'dup' }; setErr(''); setD(p => ({ ...p, [k]: [...p[k], s] })); return null }
  const rem = (k: K) => (s: string) => setD(p => ({ ...p, [k]: p[k].filter(x => x !== s) }))
  const set = (k: keyof User) => (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => setD({ ...d, [k]: e.target.value })
  const save = async () => { try { setUser((await api.put<User>('/users/me', d)).data); setErr(''); setOk(true); setTimeout(() => setOk(false), 2000) } catch (e) { setErr(errMsg(e)) } }
  return <div className="wrap"><h2>My Profile</h2><div className="card">
    <div className="row" style={{ marginBottom: 10 }}><Avatar u={d} size={64} />{AVATARS.map(a => <button key={a} className="btn g sm" onClick={() => setD({ ...d, avatar: a })}>{a}</button>)}</div>
    <label>Name</label><input value={d.name} onChange={set('name')} /><label>College / Organization</label><input value={d.college ?? ''} onChange={set('college')} />
    <label>Location</label><input value={d.location ?? ''} onChange={set('location')} /><label>Bio</label><textarea value={d.bio ?? ''} onChange={set('bio')} />
    <h3>Skills I Know</h3><Chips list={d.skillsKnown} onRemove={rem('skillsKnown')} /><SkillPicker onAdd={add('skillsKnown')} />
    <h3>Want to Learn</h3><Chips list={d.skillsWanted} w onRemove={rem('skillsWanted')} /><SkillPicker onAdd={add('skillsWanted')} />
    {err && <div className="err">{err}</div>}{ok && <div style={{ color: 'var(--ok)' }}>Profile saved ✓</div>}<button className="btn" style={{ marginTop: 12 }} onClick={save}>Save Changes</button></div></div>
}
