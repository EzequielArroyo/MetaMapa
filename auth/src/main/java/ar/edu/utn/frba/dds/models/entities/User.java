package ar.edu.utn.frba.dds.models.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import lombok.NoArgsConstructor;

@Entity(name = "user")
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  private String username;
  @Column
  private String name;
  @Column
  private String passwordHash;
  @Column
  private String email;
  @Enumerated(EnumType.STRING)
  private Role role;
  @Builder.Default
  @Enumerated(EnumType.STRING) @ElementCollection() @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
  private List<Permission> permissions = new ArrayList<>();
  @Builder.Default
  @Enumerated(EnumType.STRING) @Column(nullable = false)
  private UserStatus status = UserStatus.ACTIVE;
}

