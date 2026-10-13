import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { api, errMsg, fmt } from '../api'
import { useAuth } from '../AuthContext'
import { Exchange, Message, User } from '../types'
import { Avatar, Empty } from '../components/Ui'
export default function Messages() {
  const { user } = useAuth(), [params, setParams] = useSearchParams(), [partners, setPartners] = useState<User[]>([]), [msgs, setMsgs] = useState<Message[]>([]), [text, setText] = useState(''), [err, setErr] = useState('')
  const withId = Number(params.get('with')) || null, cur = partners.find(p => p.id === withId)
  useEffect(() => { api.get<Exchange[]>('/exchanges').then(r => setPartners([...new Map(r.data.map(x => [x.partner.id, x.partner])).values()])) }, [])
  const load = () => { if (withId) api.get<Message[]>(`/messages/${withId}`).then(r => setMsgs(r.data)).catch(e => setErr(errMsg(e))) }
  useEffect(() => { load(); const t = setInterval(load, 5000); return () => clearInterval(t) }, [withId])   // polling; swap for WebSocket later
  useEffect(() => { const el = document.getElementById('msgs'); if (el) el.scrollTop = el.scrollHeight }, [msgs.length])
  const send = async () => { if (!text.trim() || !withId) return; try { await api.post('/messages', { receiverId: withId, text }); setText(''); load() } catch (e) { setErr(errMsg(e)) } }
  if (!partners.length) return <div className="wrap"><h2>Messages</h2><Empty t="Start a conversation with one of your skill partners. Accept a request first to unlock chat." /></div>
  return <div className="wrap"><h2>Messages</h2><div className="chat"><div className="card">{partners.map(p => <div key={p.id} className={'pl row' + (withId === p.id ? ' on' : '')} onClick={() => setParams({ with: String(p.id) })}><Avatar u={p} size={36} />{p.name}</div>)}</div>
    <div className="card">{cur ? <><b>{cur.name}</b><div className="msgs" id="msgs">{msgs.length ? msgs.map(m => <div key={m.id} className={'bub' + (m.senderId === user!.id ? ' me' : '')}>{m.text}<small>{fmt(m.timestamp)}</small></div>) : <div className="empty">Say hi to {cur.name.split(' ')[0]}!</div>}</div>
      {err && <div className="err">{err}</div>}<div className="row"><input style={{ flex: 1, margin: 0 }} placeholder="Type a message…" value={text} onChange={e => setText(e.target.value)} onKeyDown={e => e.key === 'Enter' && send()} /><button className="btn" onClick={send}>Send</button></div></> : <div className="empty">Select a conversation.</div>}</div></div></div>
}
