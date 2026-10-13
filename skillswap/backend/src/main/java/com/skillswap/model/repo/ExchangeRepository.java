package com.skillswap.model.repo;
import com.skillswap.model.Exchange; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.List;
public interface ExchangeRepository extends JpaRepository<Exchange,Long> {
  @Query("select e from Exchange e where e.user1Id=:id or e.user2Id=:id order by e.createdAt desc") List<Exchange> findForUser(@Param("id") Long id);
  @Query("select count(e)>0 from Exchange e where (e.user1Id=:a and e.user2Id=:b) or (e.user1Id=:b and e.user2Id=:a)") boolean existsBetween(@Param("a") Long a,@Param("b") Long b);
}
