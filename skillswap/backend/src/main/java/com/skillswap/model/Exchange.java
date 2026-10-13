package com.skillswap.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant; import java.util.*;
@Entity @Table(name="exchanges") @Getter @Setter @NoArgsConstructor
public class Exchange {
  public enum Status { ACTIVE, COMPLETED }
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  private Long requestId; private Long user1Id; private Long user2Id;   // user1 = request sender
  @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="exchange_user1_teaches") private Set<String> user1Teaches=new LinkedHashSet<>();
  @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="exchange_user2_teaches") private Set<String> user2Teaches=new LinkedHashSet<>();
  @Enumerated(EnumType.STRING) private Status status=Status.ACTIVE;
  private Instant createdAt=Instant.now();
}
