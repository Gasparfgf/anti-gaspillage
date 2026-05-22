package backend.antigasp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import backend.antigasp.model.enumsBusiness.Role;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String firstname;
    private String surname;
    private String email;
    private String address;
    private LocalDate birthDate;
    private Role role;
}
