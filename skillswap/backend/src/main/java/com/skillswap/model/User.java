package com.skillswap.model;
import jakarta.persistence.*; import lombok.*; import java.util.*;
@Entity @Table(name="users") @Getter @Setter @NoArgsConstructor
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String name;
  @Column(nullable=false,unique=true) private String email;
  @Column(nullable=false) private String passwordHash;
  private String college; private String location; private String avatar;
  @Column(length=500) private String bio;
  @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="user_skills_known") private Set<String> skillsKnown=new LinkedHashSet<>();
  @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="user_skills_wanted") private Set<String> skillsWanted=new LinkedHashSet<>();
}
