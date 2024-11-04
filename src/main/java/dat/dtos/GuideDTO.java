package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.Guide;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuideDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int yearsOfExperience;
    private Set<TripDTO> trips = new HashSet<>();

    // Constructor for convenient initialization
    public GuideDTO(Long id, String firstName, String lastName, String email, String phone, int yearsOfExperience) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }

    public GuideDTO(Guide guide) {
        this.id = guide.getId();
        this.firstName = guide.getFirstName();
        this.lastName = guide.getLastName();
        this.email = guide.getEmail();
        this.phone = guide.getPhone();
        this.yearsOfExperience = guide.getYearsOfExperience();

        // Convert trips to TripDTOs if trips are not null
        if (guide.getTrips() != null) {
            guide.getTrips().forEach(trip -> this.trips.add(new TripDTO(trip)));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuideDTO guideDTO = (GuideDTO) o;
        return Objects.equals(id, guideDTO.id) &&
                Objects.equals(email, guideDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
