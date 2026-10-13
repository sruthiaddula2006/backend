package com.skillswap.model.repo;
import com.skillswap.model.Message; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.List;
public interface MessageRepository extends JpaRepository<Message,Long> {
  @Query("select m from Message m where (m.senderId=:a and m.receiverId=:b) or (m.senderId=:b and m.receiverId=:a) order by m.timestamp") List<Message> conversation(@Param("a") Long a,@Param("b") Long b);
}
