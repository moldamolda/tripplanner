package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.Trip;
import dat.enums.TripCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDTO {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String startPosition;
    private String name;
    private double price;
    private TripCategory category;
    private Long guideId; // Guide ID for reference


    // Constructor for convenient initialization
    public TripDTO(Long id, LocalDateTime startTime, LocalDateTime endTime, String startPosition, String name, double price, TripCategory category, Long guideId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this.name = name;
        this.price = price;
        this.category = category;
        this.guideId = guideId;
    }

    // Constructor to initialize from a Trip entity
    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.startTime = trip.getStartTime();
        this.endTime = trip.getEndTime();
        this.startPosition = trip.getStartPosition();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.category = trip.getCategory();
        if (trip.getGuide() != null) {
            this.guideId = trip.getGuide().getId(); // Initialize guideId from the Guide entity
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripDTO tripDTO = (TripDTO) o;
        return Objects.equals(id, tripDTO.id) &&
                Objects.equals(name, tripDTO.name) &&
                Objects.equals(startTime, tripDTO.startTime) &&
                Objects.equals(endTime, tripDTO.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startTime, endTime);
    }
}
