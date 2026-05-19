package backend.antigasp.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfferResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Long merchantId;
    private String merchantName;
}
