import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api'
import { Match } from '../types'
import { Avatar, Chips, Empty, Ring } from '../components/Ui'
import SendRequestModal from '../components/SendRequestModal'
export default function UserProfile() {
  const { id } = useParams(), [m, setM] = useState<Match | null | undefined>(undefined), [open, setOpen] = useState(false)
  useEffect(() => { api.get<Match[]>('/matches').then(r => setM(r.data.find(x => x.user.id === Number(id)) ?? null)).catch(() => setM(null)) }, [id])
  if (m === undefined) return <div className="empty">Loading…</div>
  if (m === null) return <Empty t="User not found." />
  const u = m.user
  return <div className="wrap"><div className="card"><div className="row"><Avatar u={u} size={80} /><div className="sp"><h2 style={{ margin: 0 }}>{u.name}</h2><div className="mu">{u.college} · {u.location}</div></div><Ring v={m.score} /></div>
    <p>{u.bio}</p><h3>Skills</h3><Chips list={u.skillsKnown} /><h3>Learning goals</h3><Chips list={u.skillsWanted} w /><h3>Shared skill opportunities</h3><p>{m.reason}</p>
    <button className="btn" onClick={() => setOpen(true)}>Send Skill Exchange Request</button> <Link to="/matches" className="btn g">Back</Link></div>
    {open && <SendRequestModal to={u} onClose={() => setOpen(false)} />}</div>
}
