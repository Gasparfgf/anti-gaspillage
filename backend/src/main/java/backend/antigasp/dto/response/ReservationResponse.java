package backend.antigasp.dto.response;

import backend.antigasp.model.enumsBusiness.ReservationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationResponse {
    private Long id;
    private Long offerId;
    private String offerTitle;
    private Long userId;
    private String userName;
    private ReservationStatus status;
}
