package ar.edu.utn.frba.dds.service.impl;

import ar.edu.utn.frba.dds.dto.output.AuthResponse;
import ar.edu.utn.frba.dds.dto.input.LoginRequest;
import ar.edu.utn.frba.dds.dto.input.RefreshRequest;
import ar.edu.utn.frba.dds.dto.input.SignUpRequest;
import ar.edu.utn.frba.dds.dto.UserRolesPermissionsDTO;
import ar.edu.utn.frba.dds.exception.InvalidCredentialsException;
import ar.edu.utn.frba.dds.exception.InvalidTokenException;
import ar.edu.utn.frba.dds.exception.UserBlockedException;
import ar.edu.utn.frba.dds.models.entities.User;
import ar.edu.utn.frba.dds.models.entities.UserStatus;
import ar.edu.utn.frba.dds.service.IAuthService;
import ar.edu.utn.frba.dds.service.IUserService;
import ar.edu.utn.frba.dds.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService implements IAuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final IUserService userService;

    public AuthService(PasswordEncoder passwordEncoder, JwtUtil jwtUtil, IUserService userService) {
      this.passwordEncoder = passwordEncoder;
      this.jwtUtil = jwtUtil;
      this.userService = userService;
    }

  public AuthResponse login(LoginRequest credentials) {

    User user = authenticate(credentials.getUsername(), credentials.getPassword());
    String accessToken = generarAccessToken(user);
    String refreshToken = generarRefreshToken(user);

    return new AuthResponse(accessToken, refreshToken);
  }


  private User authenticate(String username, String password) {

    User user = userService.getUserByUsername(username);

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new InvalidCredentialsException();
    }

    if (user.getStatus() != UserStatus.ACTIVE) {
      throw new UserBlockedException();
    }

    return user;
  }



  @Override
  public User signUp(SignUpRequest request) {
    return userService.createUser(request);
  }

  @Override
  public AuthResponse refresh(RefreshRequest request) {

    Long userId = Long.valueOf(
        jwtUtil.getUserIdFromRefreshToken(request.getRefreshToken())
    );

    User user = userService.getUserById(userId);

    if (user.getStatus() != UserStatus.ACTIVE) {
      throw new InvalidTokenException();
    }

    String newAccessToken = jwtUtil.generarAccessToken(user);

    return new AuthResponse(newAccessToken, request.getRefreshToken());
  }

    private String generarAccessToken(User user) {
        return jwtUtil.generarAccessToken(user);
    }

    private String generarRefreshToken(User user) {
        return jwtUtil.generarRefreshToken(user);
    }

  public UserRolesPermissionsDTO obtenerRolesYPermisosUsuario(Long userId) {

    User user = userService.getUserById(userId);

    if (user.getStatus() != UserStatus.ACTIVE) {
      throw new UserBlockedException();
    }

    return new UserRolesPermissionsDTO(user.getRole(), user.getPermissions());
  }
}
