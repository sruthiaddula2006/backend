package com.skillswap.model.repo;
import com.skillswap.model.Notification; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import org.springframework.transaction.annotation.Transactional; import java.util.List;
public interface NotificationRepository extends JpaRepository<Notification,Long> {
  List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
  boolean existsByUserIdAndTypeAndRefId(Long userId,String type,Long refId);
  @Modifying @Transactional @Query("update Notification n set n.seen=true where n.userId=:uid") void markAllSeen(@Param("uid") Long uid);
}
