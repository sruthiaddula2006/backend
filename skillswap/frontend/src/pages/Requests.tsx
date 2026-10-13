import { useCallback, useEffect, useState } from 'react'
import { api, errMsg, fmt } from '../api'
import { useAuth } from '../AuthContext'
import { SwapRequest } from '../types'
import { Empty } from '../components/Ui'
export default function Requests() {
  const { user } = useAuth(), [tab, setTab] = useState<'in' | 'out'>('in'), [all, setAll] = useState<SwapRequest[]>([]), [err, setErr] = useState('')
  const load = useCallback(() => { api.get<SwapRequest[]>('/requests').then(r => setAll(r.data)) }, [])
  useEffect(load, [load])
  const run = (p: Promise<unknown>) => p.then(load).catch(e => setErr(errMsg(e)))
  const list = all.filter(r => tab === 'in' ? r.receiverId === user!.id : r.senderId === user!.id)
  return <div className="wrap"><h2>Requests</h2><div className="row" style={{ marginBottom: 12 }}><button className={'btn sm' + (tab === 'in' ? '' : ' g')} onClick={() => setTab('in')}>Received</button><button className={'btn sm' + (tab === 'out' ? '' : ' g')} onClick={() => setTab('out')}>Sent</button></div>
    {err && <div className="err">{err}</div>}
    {list.length ? <div className="grid">{list.map(r => <div className="card" key={r.id}><div className="row"><b>{tab === 'in' ? r.senderName : 'To ' + r.receiverName}</b><span className="sp" /><span className="chip">{r.status}</span></div>
      <p>"{r.message}"</p><div className="mu">Offered: {r.skillsOffered.join(', ') || '—'} · Requested: {r.skillsRequested.join(', ') || '—'}</div><div className="mu">{fmt(r.createdAt)}</div>
      {r.status === 'PENDING' && <div className="row" style={{ marginTop: 10 }}>{tab === 'in' ? <><button className="btn ok sm" onClick={() => run(api.put(`/requests/${r.id}/accept`))}>Accept</button><button className="btn er sm" onClick={() => run(api.put(`/requests/${r.id}/reject`))}>Reject</button></> : <button className="btn er sm" onClick={() => run(api.delete(`/requests/${r.id}`))}>Cancel</button>}</div>}</div>)}</div>
      : <Empty t={tab === 'in' ? "You don't have any pending requests." : "You haven't sent any requests yet."} />}</div>
}
