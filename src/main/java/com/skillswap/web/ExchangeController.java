package com.skillswap.web;
import com.skillswap.dto.Dtos.*; import com.skillswap.model.Exchange; import com.skillswap.model.repo.*; import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.util.*;
@RestController @RequestMapping("/api/exchanges") @RequiredArgsConstructor
public class ExchangeController {
  private final ExchangeRepository exchanges; private final UserRepository users;
  private ExchangeDto dto(Exchange e,Long uid){ boolean first=e.getUser1Id().equals(uid); Long pid=first?e.getUser2Id():e.getUser1Id();
    return new ExchangeDto(e.getId(),UserDto.from(users.findById(pid).orElseThrow()),new LinkedHashSet<>(first?e.getUser1Teaches():e.getUser2Teaches()),new LinkedHashSet<>(first?e.getUser2Teaches():e.getUser1Teaches()),e.getStatus().name(),e.getCreatedAt()); }
  @GetMapping public List<ExchangeDto> mine(@AuthenticationPrincipal Long uid){ return exchanges.findForUser(uid).stream().map(e->dto(e,uid)).toList(); }
  @PutMapping("/{id}/complete") @Transactional
  public ExchangeDto complete(@AuthenticationPrincipal Long uid,@PathVariable Long id){
    Exchange e=exchanges.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Exchange not found."));
    if(!uid.equals(e.getUser1Id())&&!uid.equals(e.getUser2Id())) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Not your exchange.");
    e.setStatus(Exchange.Status.COMPLETED); return dto(exchanges.save(e),uid); }
}
