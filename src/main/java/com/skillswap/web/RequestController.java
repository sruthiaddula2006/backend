package com.skillswap.web;
import com.skillswap.dto.Dtos.*; import com.skillswap.model.*; import com.skillswap.model.repo.*; import com.skillswap.service.*;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.util.*;
@RestController @RequestMapping("/api/requests") @RequiredArgsConstructor
public class RequestController {
  private final SwapRequestRepository requests; private final UserRepository users; private final ExchangeRepository exchanges; private final MatchService matcher; private final NotificationService notifs;
  private RequestDto dto(SwapRequest r){ return new RequestDto(r.getId(),r.getSenderId(),users.findById(r.getSenderId()).map(User::getName).orElse("?"),r.getReceiverId(),users.findById(r.getReceiverId()).map(User::getName).orElse("?"),new LinkedHashSet<>(r.getSkillsOffered()),new LinkedHashSet<>(r.getSkillsRequested()),r.getMessage(),r.getStatus().name(),r.getCreatedAt()); }
  private static ResponseStatusException err(HttpStatus s,String m){ return new ResponseStatusException(s,m); }
  @GetMapping public List<RequestDto> mine(@AuthenticationPrincipal Long uid){ return requests.findByReceiverIdOrSenderIdOrderByCreatedAtDesc(uid,uid).stream().map(this::dto).toList(); }
  @PostMapping @Transactional
  public RequestDto send(@AuthenticationPrincipal Long uid,@Valid @RequestBody SendRequestReq req){
    if(uid.equals(req.receiverId())) throw err(HttpStatus.BAD_REQUEST,"You cannot send a request to yourself.");
    User me=users.findById(uid).orElseThrow(), to=users.findById(req.receiverId()).orElseThrow(()->err(HttpStatus.NOT_FOUND,"User not found."));
    if(requests.existsBySenderIdAndReceiverIdAndStatus(uid,to.getId(),SwapRequest.Status.PENDING)) throw err(HttpStatus.CONFLICT,"Request already sent.");
    MatchService.Result m=matcher.compute(me,to); SwapRequest r=new SwapRequest(); r.setSenderId(uid); r.setReceiverId(to.getId()); r.setMessage(req.message().trim());
    r.getSkillsOffered().addAll(m.iTeach()); r.getSkillsRequested().addAll(m.iLearn()); requests.save(r);
    notifs.push(to.getId(),"request",me.getName()+" sent you a skill exchange request.",r.getId()); return dto(r); }
  @PutMapping("/{id}/accept") @Transactional public RequestDto accept(@AuthenticationPrincipal Long uid,@PathVariable Long id){ return respond(uid,id,true); }
  @PutMapping("/{id}/reject") @Transactional public RequestDto reject(@AuthenticationPrincipal Long uid,@PathVariable Long id){ return respond(uid,id,false); }
  private RequestDto respond(Long uid,Long id,boolean accept){
    SwapRequest r=requests.findById(id).orElseThrow(()->err(HttpStatus.NOT_FOUND,"Request not found."));
    if(!r.getReceiverId().equals(uid)) throw err(HttpStatus.FORBIDDEN,"Only the receiver can respond.");
    if(r.getStatus()!=SwapRequest.Status.PENDING) throw err(HttpStatus.CONFLICT,"Request already answered.");
    r.setStatus(accept?SwapRequest.Status.ACCEPTED:SwapRequest.Status.REJECTED); requests.save(r);
    if(accept){ Exchange e=new Exchange(); e.setRequestId(r.getId()); e.setUser1Id(r.getSenderId()); e.setUser2Id(r.getReceiverId()); e.getUser1Teaches().addAll(r.getSkillsOffered()); e.getUser2Teaches().addAll(r.getSkillsRequested()); exchanges.save(e); }
    notifs.push(r.getSenderId(),"response",users.findById(uid).orElseThrow().getName()+(accept?" accepted":" rejected")+" your request.",r.getId()); return dto(r); }
  @DeleteMapping("/{id}") @Transactional
  public void cancel(@AuthenticationPrincipal Long uid,@PathVariable Long id){
    SwapRequest r=requests.findById(id).orElseThrow(()->err(HttpStatus.NOT_FOUND,"Request not found."));
    if(!r.getSenderId().equals(uid)||r.getStatus()!=SwapRequest.Status.PENDING) throw err(HttpStatus.FORBIDDEN,"Only your pending requests can be cancelled.");
    requests.delete(r); }
}
