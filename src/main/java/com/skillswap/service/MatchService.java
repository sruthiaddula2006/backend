package com.skillswap.service;
import com.skillswap.dto.Dtos.*; import com.skillswap.model.User; import com.skillswap.model.repo.UserRepository; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service;
import java.util.*; import java.util.stream.Collectors;
/**
 * Transparent, rule-based matching (no ML). Score out of 100:
 *   70 pts  how much of each person's wanted list the other can teach (averaged both ways)
 *   15 pts  mutual-exchange bonus (each can teach the other something)
 *    6 pts  common interests (3 per shared skill, capped)
 *    5 pts  same location
 */
@Service @RequiredArgsConstructor
public class MatchService {
  private final UserRepository users;
  public record Result(int score,List<String> iTeach,List<String> iLearn,String reason){}

  private static List<String> overlap(Set<String> source,Set<String> filter){
    Set<String> f=filter.stream().map(String::toLowerCase).collect(Collectors.toSet());
    return source.stream().filter(s->f.contains(s.toLowerCase())).toList(); }
  private static boolean anyContains(Set<String> s,String t){ return s.stream().anyMatch(x->x.toLowerCase().contains(t)); }

  public Result compute(User a,User b){
    List<String> iLearn=overlap(b.getSkillsKnown(),a.getSkillsWanted()), iTeach=overlap(a.getSkillsKnown(),b.getSkillsWanted());
    int common=overlap(a.getSkillsKnown(),b.getSkillsKnown()).size()+overlap(a.getSkillsWanted(),b.getSkillsWanted()).size();
    double x=a.getSkillsWanted().isEmpty()?0:(double)iLearn.size()/a.getSkillsWanted().size();
    double y=b.getSkillsWanted().isEmpty()?0:(double)iTeach.size()/b.getSkillsWanted().size();
    double score=(x+y)/2*70;
    if(!iLearn.isEmpty()&&!iTeach.isEmpty()) score+=15;
    score+=Math.min(common*3,6);
    if(a.getLocation()!=null&&a.getLocation().equalsIgnoreCase(b.getLocation())) score+=5;
    String f=b.getName().split(" ")[0];
    String reason=!iTeach.isEmpty()&&!iLearn.isEmpty()?"You can teach "+String.join(", ",iTeach)+" to "+f+", and "+f+" can teach "+String.join(", ",iLearn)+" to you."
      :!iLearn.isEmpty()?f+" can teach you "+String.join(", ",iLearn)+".":!iTeach.isEmpty()?"You can teach "+String.join(", ",iTeach)+" to "+f+".":"Shared interests only.";
    return new Result((int)Math.round(Math.min(100,score)),iTeach,iLearn,reason); }

  /** Ranked matches for `me`, optionally filtered by text/skill/location/min score. */
  public List<MatchDto> matchesFor(User me,String q,String skill,String location,int minScore){
    String t=q==null?"":q.trim().toLowerCase(), sk=skill==null?"":skill.trim().toLowerCase(), loc=location==null?"":location.trim().toLowerCase();
    return users.findAll().stream().filter(u->!u.getId().equals(me.getId())).map(u->{ Result r=compute(me,u); return new MatchDto(UserDto.from(u),r.score(),r.iTeach(),r.iLearn(),r.reason()); })
      .filter(m->m.score()>=minScore)
      .filter(m->t.isEmpty()||m.user().name().toLowerCase().contains(t)||anyContains(m.user().skillsKnown(),t)||anyContains(m.user().skillsWanted(),t))
      .filter(m->sk.isEmpty()||anyContains(m.user().skillsKnown(),sk)||anyContains(m.user().skillsWanted(),sk))
      .filter(m->loc.isEmpty()||(m.user().location()!=null&&m.user().location().toLowerCase().contains(loc)))
      .sorted(Comparator.comparingInt(MatchDto::score).reversed()).toList(); }
}
