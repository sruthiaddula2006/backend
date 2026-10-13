import { useCallback, useEffect, useState } from 'react'
import { api, fmt } from '../api'
import { Notif } from '../types'
import { Empty } from '../components/Ui'
export default function Notifications() {
  const [list, setList] = useState<Notif[]>([])
  const load = useCallback(() => { api.get<Notif[]>('/notifications').then(r => setList(r.data)) }, [])
  useEffect(load, [load])
  return <div className="wrap"><div className="row"><h2 className="sp">Notifications</h2><button className="btn g sm" onClick={() => api.put('/notifications/read-all').then(load)}>Mark all read</button></div>
    {list.length ? list.map(n => <div className="card" key={n.id} style={{ marginBottom: 10, opacity: n.read ? .6 : 1 }}>{n.message}<div className="mu">{fmt(n.createdAt)}</div></div>) : <Empty t="You're all caught up." />}</div>
}
