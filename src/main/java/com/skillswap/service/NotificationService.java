package com.skillswap.service;
import com.skillswap.model.Notification; import com.skillswap.model.repo.NotificationRepository; import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class NotificationService {
  private final NotificationRepository repo;
  public void push(Long userId,String type,String message,Long refId){ Notification n=new Notification(); n.setUserId(userId); n.setType(type); n.setMessage(message); n.setRefId(refId); repo.save(n); }
}
