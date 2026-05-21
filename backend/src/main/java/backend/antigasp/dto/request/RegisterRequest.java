package backend.antigasp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {

    @NotBlank(message = "The firstname is mandatory")
    private String firstname;

    @NotBlank(message = "The surname is mandatory")
    private String surname;

    @NotBlank(message = "The address is mandatory")
    private String address;

    @NotNull(message = "The birth date is mandatory")
    @Past(message = "The date of birth must be in the past")
    private LocalDate birthDate;

    @NotBlank(message = "The email is mandatory")
    @Email(message = "The email format is not valid")
    private String email;

    @NotBlank(message = "The password is mandatory")
    @Size(min = 8, message = "The password must contain at least 8 characters.")
    private String password;
}
