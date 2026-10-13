import { useState } from 'react'
import { CATALOG } from '../skills'
import { User } from '../types'
export const Avatar = ({ u, size }: { u: Pick<User,'avatar'>; size?: number }) => <div className="av" style={size ? { width: size, height: size, fontSize: size / 2 } : {}}>{u.avatar}</div>
export const Ring = ({ v }: { v: number }) => <div className="ring" style={{ '--p': v } as React.CSSProperties}><span>{v}%</span></div>
export const Chips = ({ list, w, onRemove }: { list: string[]; w?: boolean; onRemove?: (s: string) => void }) =>
  <>{list.length ? list.map(s => <span key={s} className={'chip' + (w ? ' w' : '')}>{s}{onRemove && <span className="x" onClick={() => onRemove(s)}>×</span>}</span>) : <span className="mu">None yet</span>}</>
export const Empty = ({ t }: { t: string }) => <div className="empty card">{t}</div>
/** onAdd returns an error message (or null on success) */
export function SkillPicker({ onAdd }: { onAdd: (s: string) => string | null }) {
  const [sel, setSel] = useState(''), [custom, setCustom] = useState('')
  return <div className="row"><select style={{ flex: 1, minWidth: 140 }} value={sel} onChange={e => setSel(e.target.value)}><option value="">Choose a skill…</option>
    {Object.entries(CATALOG).map(([c, l]) => <optgroup key={c} label={c}>{l.map(s => <option key={c + s}>{s}</option>)}</optgroup>)}</select>
    <input style={{ flex: 1, minWidth: 120 }} placeholder="or custom skill" value={custom} onChange={e => setCustom(e.target.value)} />
    <button className="btn sm" onClick={() => { const s = (custom.trim() || sel); if (s && !onAdd(s)) { setSel(''); setCustom('') } }}>Add Skill</button></div>
}
