import { useEffect, useState } from 'react'
import { api } from '../api'
import { useAuth } from '../AuthContext'
import { Exchange, Match, SwapRequest } from '../types'
import { Avatar, Chips, Empty } from '../components/Ui'
import MatchCard from '../components/MatchCard'
export default function Dashboard() {
  const { user } = useAuth(), [ms, setMs] = useState<Match[]>([]), [reqs, setReqs] = useState<SwapRequest[]>([]), [ex, setEx] = useState<Exchange[]>([])
  useEffect(() => { api.get('/matches').then(r => setMs(r.data)); api.get('/requests').then(r => setReqs(r.data)); api.get('/exchanges').then(r => setEx(r.data)) }, [])
  if (!user) return null
  return <div className="wrap"><h1>Welcome back, {user.name.split(' ')[0]}! 👋</h1>
    <div className="grid"><div className="card"><div className="mu">Potential Matches</div><div className="big">{ms.filter(m => m.score >= 40).length}</div></div>
      <div className="card"><div className="mu">Pending Requests</div><div className="big">{reqs.filter(r => r.receiverId === user.id && r.status === 'PENDING').length}</div></div>
      <div className="card"><div className="mu">Accepted Exchanges</div><div className="big">{ex.filter(e => e.status === 'ACTIVE').length}</div></div></div>
    <div className="grid" style={{ margin: '16px 0' }}><div className="card row"><Avatar u={user} /><div><b>{user.name}</b><div className="mu">{user.college} · {user.location}</div><div className="mu">{user.bio}</div></div></div>
      <div className="card"><b>My Skills</b><div><Chips list={user.skillsKnown} /></div><b>I Want to Learn</b><div><Chips list={user.skillsWanted} w /></div></div></div>
    <h2>Recommended matches</h2>{ms.length && ms[0].score > 0 ? <div className="grid">{ms.slice(0, 3).map(m => <MatchCard key={m.user.id} m={m} />)}</div> : <Empty t="No skill matches found yet. Try adding more skills to your profile." />}</div>
}
