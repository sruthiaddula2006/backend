package com.skillswap.web;
import com.skillswap.dto.Dtos.*; import com.skillswap.model.User; import com.skillswap.model.repo.*; import com.skillswap.service.*;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.util.*; import java.util.stream.Collectors;
@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class UserController {
  private final UserRepository users; private final NotificationRepository notifRepo; private final NotificationService notifs; private final MatchService matcher;
  private static Set<String> clean(Set<String> in){ return in==null?new LinkedHashSet<>():in.stream().map(String::trim).filter(s->!s.isEmpty()&&s.length()<=40).collect(Collectors.toCollection(LinkedHashSet::new)); }
  @GetMapping("/users/me") public UserDto me(@AuthenticationPrincipal Long uid){ return UserDto.from(users.findById(uid).orElseThrow()); }
  @GetMapping("/users/{id}") public UserDto get(@PathVariable Long id){ return UserDto.from(users.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."))); }
  @PutMapping("/users/me") @Transactional
  public UserDto update(@AuthenticationPrincipal Long uid,@Valid @RequestBody ProfileReq r){
    User u=users.findById(uid).orElseThrow(); u.setName(r.name().trim()); u.setCollege(r.college()); u.setLocation(r.location()); u.setBio(r.bio()); if(r.avatar()!=null) u.setAvatar(r.avatar());
    u.getSkillsKnown().clear(); u.getSkillsKnown().addAll(clean(r.skillsKnown())); u.getSkillsWanted().clear(); u.getSkillsWanted().addAll(clean(r.skillsWanted()));
    users.save(u);
    matcher.matchesFor(u,null,null,null,80).stream().limit(2).filter(m->!notifRepo.existsByUserIdAndTypeAndRefId(uid,"match",m.user().id()))
      .forEach(m->notifs.push(uid,"match","New strong match: "+m.user().name()+" ("+m.score()+"%)",m.user().id()));
    return UserDto.from(u); }
  /** Ranked matches; also powers search: ?q=python&skill=Java&location=hyd&minScore=40 */
  @GetMapping("/matches") public List<MatchDto> matches(@AuthenticationPrincipal Long uid,@RequestParam(required=false) String q,@RequestParam(required=false) String skill,@RequestParam(required=false) String location,@RequestParam(defaultValue="0") int minScore){
    return matcher.matchesFor(users.findById(uid).orElseThrow(),q,skill,location,minScore); }
}
