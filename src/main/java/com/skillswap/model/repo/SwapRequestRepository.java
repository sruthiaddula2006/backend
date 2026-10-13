package com.skillswap.model.repo;
import com.skillswap.model.SwapRequest; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface SwapRequestRepository extends JpaRepository<SwapRequest,Long> {
  List<SwapRequest> findByReceiverIdOrSenderIdOrderByCreatedAtDesc(Long receiverId, Long senderId);
  boolean existsBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, SwapRequest.Status status);
}
