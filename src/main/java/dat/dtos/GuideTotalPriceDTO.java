package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GuideTotalPriceDTO {
    private Long guideId;
    private Double totalPrice;
}
