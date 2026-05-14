package backend.antigasp.dto.response;

import backend.antigasp.model.enumsBusiness.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String email;
    private String firstname;
    private Role role;
}
