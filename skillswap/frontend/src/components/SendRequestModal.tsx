import { useState } from 'react'
import { api, errMsg } from '../api'
import { User } from '../types'
export default function SendRequestModal({ to, onClose }: { to: User; onClose: () => void }) {
  const [msg, setMsg] = useState(`Hi ${to.name.split(' ')[0]}! I'd love to swap skills with you.`), [err, setErr] = useState(''), [done, setDone] = useState(false)
  const send = async () => { try { await api.post('/requests', { receiverId: to.id, message: msg }); setDone(true); setTimeout(onClose, 900) } catch (e) { setErr(errMsg(e)) } }
  return <div className="modal" onClick={onClose}><div className="card" style={{ maxWidth: 440, width: '100%' }} onClick={e => e.stopPropagation()}>
    <h3 style={{ marginTop: 0 }}>Request to {to.name}</h3><textarea value={msg} onChange={e => setMsg(e.target.value)} />
    {err && <div className="err">{err}</div>}{done && <div style={{ color: 'var(--ok)' }}>Request sent ✓</div>}
    <div className="row"><button className="btn" onClick={send} disabled={done}>Send Request</button><button className="btn g" onClick={onClose}>Cancel</button></div></div></div>
}
