package com.skillswap.web;
import com.skillswap.dto.Dtos.*; import com.skillswap.model.Message; import com.skillswap.model.repo.*; import com.skillswap.service.NotificationService;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.util.List;
/** Chat is only allowed between users who share an exchange (i.e. an accepted request). */
@RestController @RequestMapping("/api/messages") @RequiredArgsConstructor
public class MessageController {
  private final MessageRepository messages; private final ExchangeRepository exchanges; private final UserRepository users; private final NotificationService notifs;
  private void guard(Long a,Long b){ if(!exchanges.existsBetween(a,b)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You can only chat with accepted skill partners."); }
  private static MessageDto dto(Message m){ return new MessageDto(m.getId(),m.getSenderId(),m.getReceiverId(),m.getText(),m.getTimestamp()); }
  @GetMapping("/{otherId}") public List<MessageDto> conversation(@AuthenticationPrincipal Long uid,@PathVariable Long otherId){ guard(uid,otherId); return messages.conversation(uid,otherId).stream().map(MessageController::dto).toList(); }
  @PostMapping public MessageDto send(@AuthenticationPrincipal Long uid,@Valid @RequestBody SendMessageReq r){
    guard(uid,r.receiverId()); Message m=new Message(); m.setSenderId(uid); m.setReceiverId(r.receiverId()); m.setText(r.text().trim()); messages.save(m);
    notifs.push(r.receiverId(),"message",users.findById(uid).orElseThrow().getName()+" sent you a message.",uid); return dto(m); }
}
