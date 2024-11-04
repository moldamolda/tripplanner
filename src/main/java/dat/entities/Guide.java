package dat.entities;

import dat.dtos.GuideDTO;
import dat.enums.TripCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "guide")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guide_id", nullable = false, unique = true)
    private Long id;

    @Setter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Setter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Setter
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Setter
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Setter
    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    @OneToMany(mappedBy = "guide", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @EqualsAndHashCode.Exclude  // Exclude this to prevent circular dependency
    @ToString.Exclude  // Prevent infinite loop in toString
    private Set<Trip> trips = new HashSet<>();

    // Optional: Constructor for easier initialization
    public Guide(String firstName, String lastName, String email, String phone, int yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }

    // Conversion from DTO
    public Guide(GuideDTO guideDTO) {
        this.id = guideDTO.getId();
        this.firstName = guideDTO.getFirstName();
        this.lastName = guideDTO.getLastName();
        this.email = guideDTO.getEmail();
        this.phone = guideDTO.getPhone();
        this.yearsOfExperience = guideDTO.getYearsOfExperience();
        // Trips can be added later to maintain bidirectional association if needed.
    }

    // Helper method to add a trip
    public void addTrip(Trip trip) {
        trips.add(trip);
        trip.setGuide(this);  // Maintain bidirectional relationship
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guide guide = (Guide) o;
        return Objects.equals(id, guide.id) &&
                Objects.equals(email, guide.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
