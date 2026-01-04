package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.input.LoginRequest;
import ar.edu.utn.frba.dds.dto.input.RefreshRequest;
import ar.edu.utn.frba.dds.dto.input.SignUpRequest;
import ar.edu.utn.frba.dds.models.entities.User;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import ar.edu.utn.frba.dds.dto.output.AuthResponse;
import ar.edu.utn.frba.dds.dto.UserRolesPermissionsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ar.edu.utn.frba.dds.service.IAuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService loginService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest credentials) {
        return ResponseEntity.ok(loginService.login(credentials));
    }
    @PostMapping("/register")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
            User response = loginService.signUp(request);
            URI location = URI.create("/users/" + response.getId());
            return ResponseEntity.created(location).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(loginService.refresh(request));
    }

    @GetMapping("/user/roles-permisos")
    public ResponseEntity<UserRolesPermissionsDTO> getUserRolesAndPermissions(
        Authentication authentication
    ) {
        Long userId = Long.valueOf(authentication.getName());

        UserRolesPermissionsDTO response =
            loginService.obtenerRolesYPermisosUsuario(userId);

        return ResponseEntity.ok(response);
    }

}

