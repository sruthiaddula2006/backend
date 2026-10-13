import { useEffect, useState } from 'react'
import { api } from '../api'
import { ALL_SKILLS } from '../skills'
import { Match } from '../types'
import { Empty } from '../components/Ui'
import MatchCard from '../components/MatchCard'
/** Find Matches + search: filters are sent to GET /api/matches */
export default function Matches() {
  const [q, setQ] = useState(''), [skill, setSkill] = useState(''), [location, setLocation] = useState(''), [minScore, setMin] = useState(0), [list, setList] = useState<Match[]>([])
  useEffect(() => { const t = setTimeout(() => api.get<Match[]>('/matches', { params: { q, skill, location, minScore } }).then(r => setList(r.data)), 250); return () => clearTimeout(t) }, [q, skill, location, minScore])
  return <div className="wrap"><h2>Find Matches</h2><div className="card"><input placeholder="Search by skill or name..." value={q} onChange={e => setQ(e.target.value)} />
    <div className="row"><select style={{ flex: 1 }} value={skill} onChange={e => setSkill(e.target.value)}><option value="">Any skill</option>{ALL_SKILLS.map(s => <option key={s}>{s}</option>)}</select>
      <input style={{ flex: 1 }} placeholder="Location" value={location} onChange={e => setLocation(e.target.value)} /></div>
    <label className="mu">Minimum match: {minScore}%</label><input type="range" min={0} max={100} step={5} value={minScore} onChange={e => setMin(+e.target.value)} style={{ padding: 0 }} /></div>
    <div style={{ marginTop: 16 }}>{list.length ? <div className="grid">{list.map(m => <MatchCard key={m.user.id} m={m} />)}</div> : <Empty t="No skill matches found yet. Try adding more skills to your profile." />}</div></div>
}
