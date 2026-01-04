package ar.edu.utn.frba.dds.service;

import ar.edu.utn.frba.dds.dto.output.AuthResponse;
import ar.edu.utn.frba.dds.dto.input.LoginRequest;
import ar.edu.utn.frba.dds.dto.input.RefreshRequest;
import ar.edu.utn.frba.dds.dto.input.SignUpRequest;
import ar.edu.utn.frba.dds.dto.UserRolesPermissionsDTO;
import ar.edu.utn.frba.dds.models.entities.User;

public interface IAuthService {
    UserRolesPermissionsDTO obtenerRolesYPermisosUsuario(Long userId);
    AuthResponse login(LoginRequest credentials);
    User signUp(SignUpRequest request);
    AuthResponse refresh(RefreshRequest request);

}
