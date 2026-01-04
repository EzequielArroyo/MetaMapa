package ar.edu.utn.frba.dds.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

  @NotBlank
  @Size(min = 3, max = 30)
  private String username;

  @NotBlank
  @Size(min = 6)
  private String password;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String name;
}

