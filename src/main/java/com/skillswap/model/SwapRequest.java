package com.skillswap.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant; import java.util.*;
@Entity @Table(name="swap_requests") @Getter @Setter @NoArgsConstructor
public class SwapRequest {
  public enum Status { PENDING, ACCEPTED, REJECTED }
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  private Long senderId; private Long receiverId;
  @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="request_skills_offered") private Set<String> skillsOffered=new LinkedHashSet<>();
  @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="request_skills_requested") private Set<String> skillsRequested=new LinkedHashSet<>();
  @Column(length=1000) private String message;
  @Enumerated(EnumType.STRING) private Status status=Status.PENDING;
  private Instant createdAt=Instant.now();
}
