package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.models.entities.Permission;
import ar.edu.utn.frba.dds.models.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesPermissionsDTO {
    private Role role;
    private List<Permission> permissions;
}
