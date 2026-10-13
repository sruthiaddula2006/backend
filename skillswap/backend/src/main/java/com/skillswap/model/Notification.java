package com.skillswap.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="notifications") @Getter @Setter @NoArgsConstructor
public class Notification {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  private Long userId; private String type; private String message; private Long refId;
  private boolean seen=false;
  private Instant createdAt=Instant.now();
}
