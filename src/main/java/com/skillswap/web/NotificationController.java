package com.skillswap.web;
import com.skillswap.dto.Dtos.NotificationDto; import com.skillswap.model.repo.NotificationRepository; import lombok.RequiredArgsConstructor; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/notifications") @RequiredArgsConstructor
public class NotificationController {
  private final NotificationRepository repo;
  @GetMapping public List<NotificationDto> mine(@AuthenticationPrincipal Long uid){ return repo.findByUserIdOrderByCreatedAtDesc(uid).stream().map(n->new NotificationDto(n.getId(),n.getType(),n.getMessage(),n.isSeen(),n.getCreatedAt())).toList(); }
  @PutMapping("/read-all") public void readAll(@AuthenticationPrincipal Long uid){ repo.markAllSeen(uid); }
}
