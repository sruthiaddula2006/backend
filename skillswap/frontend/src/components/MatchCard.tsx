import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Match } from '../types'
import { Avatar, Chips, Ring } from './Ui'
import SendRequestModal from './SendRequestModal'
export default function MatchCard({ m }: { m: Match }) {
  const [open, setOpen] = useState(false), u = m.user
  return <div className="card"><div className="row"><Avatar u={u} /><div className="sp"><b>{u.name}</b><div className="mu">{u.college} · {u.location}</div></div><Ring v={m.score} /></div>
    <p className="mu">{u.bio}</p><div><b>Knows:</b> <Chips list={u.skillsKnown} /></div><div><b>Wants:</b> <Chips list={u.skillsWanted} w /></div>
    <p><b>{m.score}% Match</b> — {m.reason}</p>
    <div className="row"><Link to={`/users/${u.id}`} className="btn g sm">View Profile</Link><button className="btn sm" onClick={() => setOpen(true)}>Send Request</button></div>
    {open && <SendRequestModal to={u} onClose={() => setOpen(false)} />}</div>
}
