import { useCallback, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../api'
import { Exchange } from '../types'
import { Chips, Empty } from '../components/Ui'
export default function Exchanges() {
  const nav = useNavigate(), [list, setList] = useState<Exchange[]>([])
  const load = useCallback(() => { api.get<Exchange[]>('/exchanges').then(r => setList(r.data)) }, [])
  useEffect(load, [load])
  return <div className="wrap"><h2>My Exchanges</h2>{list.length ? <div className="grid">{list.map(x => <div className="card" key={x.id}>
    <h3 style={{ marginTop: 0 }}>{x.youTeach.join(', ') || '?'} ⇄ {x.youLearn.join(', ') || '?'}</h3>
    <div><b>You teach:</b> <Chips list={x.youTeach} /></div><div><b>You learn:</b> <Chips list={x.youLearn} w /></div>
    <p><b>Partner:</b> {x.partner.name}<br /><b>Status:</b> {x.status}</p>
    <div className="row"><button className="btn sm" onClick={() => nav(`/messages?with=${x.partner.id}`)}>Open Chat</button>
      {x.status === 'ACTIVE' && <button className="btn ok sm" onClick={() => api.put(`/exchanges/${x.id}/complete`).then(load)}>Mark Completed</button>}</div></div>)}</div>
    : <Empty t="No active exchanges yet. Accept a request or send one from Find Matches." />}</div>
}
