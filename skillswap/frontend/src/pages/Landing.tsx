import { useNavigate } from 'react-router-dom'
import { Ring } from '../components/Ui'
export default function Landing() {
  const nav = useNavigate()
  const steps = [['1','Create a profile','List what you know and what you want to learn.'],['2','Get matched','Rule-based scoring ranks people who can teach you and want to learn from you.'],['3','Swap skills','Send a request, chat, and learn together.']]
  return <div><div className="hero"><h1>Learn. Share. Swap Skills.</h1>
    <p className="mu" style={{ maxWidth: 560, margin: '0 auto 24px', fontSize: 18 }}>Peer-to-peer learning: connect with people who can teach each other.</p>
    <div className="row" style={{ justifyContent: 'center' }}><button className="btn" onClick={() => nav('/register')}>Get Started</button><button className="btn g" onClick={() => nav('/matches')}>Explore Skills</button></div></div>
    <div className="wrap"><h2>How it works</h2><div className="grid">{steps.map(s => <div className="card" key={s[0]}><div className="big">{s[0]}</div><b>{s[1]}</b><p className="mu">{s[2]}</p></div>)}</div>
      <h2 style={{ marginTop: 36 }}>Example exchange</h2><div className="card row" style={{ justifyContent: 'center', gap: 30 }}><div>👨‍💻 <b>Rahul</b><br />Knows Java · Wants Python</div><div className="big">⇄</div><div>👩‍🎓 <b>Ananya</b><br />Knows Python · Wants Java</div><Ring v={92} /></div>
      <div className="card" style={{ marginTop: 36 }}><h2>About SkillSwap</h2><p><b>Problem:</b> Many people want to learn skills but cannot afford courses or do not know the right people to learn from.</p><p><b>Solution:</b> SkillSwap connects people who can teach each other.</p></div></div></div>
}
