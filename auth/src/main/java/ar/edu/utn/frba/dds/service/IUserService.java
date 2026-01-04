package ar.edu.utn.frba.dds.service;

import ar.edu.utn.frba.dds.dto.input.SignUpRequest;
import ar.edu.utn.frba.dds.models.entities.User;

public interface IUserService {
  User getUserByUsername(String username);
  User getUserById(Long userId);
  User createUser(SignUpRequest request);
}
