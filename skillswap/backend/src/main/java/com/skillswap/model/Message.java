package com.skillswap.model;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="messages") @Getter @Setter @NoArgsConstructor
public class Message {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  private Long senderId; private Long receiverId;
  @Column(length=2000) private String text;
  private Instant timestamp=Instant.now();
}
