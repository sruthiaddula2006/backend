package com.skillswap.web;
import com.skillswap.dto.Dtos.*; import com.skillswap.model.User; import com.skillswap.model.repo.UserRepository; import com.skillswap.security.JwtService; import com.skillswap.service.NotificationService;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
  private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt; private final NotificationService notifs;
  @PostMapping("/register")
  public AuthRes register(@Valid @RequestBody RegisterReq r){
    if(users.existsByEmailIgnoreCase(r.email().trim())) throw new ResponseStatusException(HttpStatus.CONFLICT,"This email is already registered.");
    User u=new User(); u.setName(r.name().trim()); u.setEmail(r.email().trim().toLowerCase()); u.setPasswordHash(encoder.encode(r.password()));
    u.setCollege(r.college().trim()); u.setLocation(r.location().trim()); u.setBio(r.bio()); u.setAvatar("🧑‍💻"); users.save(u);
    notifs.push(u.getId(),"welcome","Welcome to SkillSwap! Add skills to your profile to get matches.",null);
    return new AuthRes(jwt.create(u.getId()),UserDto.from(u)); }
  @PostMapping("/login")
  public AuthRes login(@Valid @RequestBody LoginReq r){
    User u=users.findByEmailIgnoreCase(r.email().trim()).filter(x->encoder.matches(r.password(),x.getPasswordHash())).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password."));
    return new AuthRes(jwt.create(u.getId()),UserDto.from(u)); }
}
