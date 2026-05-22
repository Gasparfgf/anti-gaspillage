package backend.antigasp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRequest {

    @NotNull(message = "The offer ID is mandatory.")
    private Long offerId;
}
