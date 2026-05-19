package backend.antigasp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OfferRequest {

    @NotBlank(message = "The title is mandatory")
    private String title;

    @NotBlank(message = "The description is mandatory")
    private String description;

    @NotNull(message = "The price is mandatory")
    @Positive(message = "The price must be positive")
    private Double price;
}
