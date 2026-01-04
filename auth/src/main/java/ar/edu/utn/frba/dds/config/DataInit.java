package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.models.entities.Permission;
import ar.edu.utn.frba.dds.models.entities.Role;
import ar.edu.utn.frba.dds.models.entities.User;
import ar.edu.utn.frba.dds.repositories.UserRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public DataInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) {
    if (userRepository.count() > 0) {
      System.out.println("Usuarios ya existen en la base de datos. No requiere inicialización.");
      return;
    }
    User adm = new User();
    adm.setUsername("admin");
    adm.setPasswordHash(passwordEncoder.encode("123"));
    adm.setEmail("adm@gmail.com");
    adm.setName("admin");
    adm.setRole(Role.ADMIN);
    adm.setPermissions(List.of(Permission.CREAR_HECHO, Permission.EDITAR_HECHO));

    User user = new User();
    user.setUsername("user");
    user.setPasswordHash(passwordEncoder.encode("123"));
    user.setEmail("user@gmail.com");
    user.setName("Usuario");
    user.setRole(Role.USER);
    user.setPermissions(List.of(Permission.CREAR_HECHO, Permission.EDITAR_HECHO));

    userRepository.save(adm);
    userRepository.save(user);

    System.out.println("Inicialización de datos completada.");
  }
}