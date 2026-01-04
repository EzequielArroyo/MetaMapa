package ar.edu.utn.frba.dds.service.impl;

import ar.edu.utn.frba.dds.dto.input.SignUpRequest;
import ar.edu.utn.frba.dds.exception.EntityAlreadyExistsException;
import ar.edu.utn.frba.dds.exception.EntityNotFoundException;
import ar.edu.utn.frba.dds.models.entities.Permission;
import ar.edu.utn.frba.dds.models.entities.Role;
import ar.edu.utn.frba.dds.models.entities.User;
import ar.edu.utn.frba.dds.models.entities.UserStatus;
import ar.edu.utn.frba.dds.repositories.UserRepository;
import ar.edu.utn.frba.dds.service.IUserService;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User"));

        if (user.getStatus() == UserStatus.DELETED) {
            throw new EntityNotFoundException("User");
        }

        return user;
    }

    @Override
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User"));

        if (user.getStatus() == UserStatus.DELETED) {
            throw new EntityNotFoundException("User");
        }

        return user;
    }

    @Override
    public User createUser(SignUpRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new EntityAlreadyExistsException("username");
        }

        User user = User.builder()
            .username(request.getUsername())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .name(request.getName())
            .role(Role.USER)
            .permissions(List.of(Permission.EDITAR_HECHO))
            .status(UserStatus.ACTIVE)
            .build();

        return userRepository.save(user);
    }
}
