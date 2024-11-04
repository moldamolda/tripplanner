package dat.entities;

import dat.dtos.TripDTO;
import dat.enums.TripCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id", nullable = false, unique = true)
    private Long id;

    @Setter
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Setter
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Setter
    @Column(name = "start_position", nullable = false)
    private String startPosition;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "price", nullable = false)
    private double price;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private TripCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    @EqualsAndHashCode.Exclude // Exclude to prevent circular dependency
    @ToString.Exclude // Prevent infinite loop in toString
    private Guide guide;

    @Column(nullable = false, updatable = false)
    @ToString.Exclude
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructor for initialization
    public Trip(LocalDateTime startTime, LocalDateTime endTime, String startPosition, String name, double price, TripCategory category, Guide guide) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this.name = name;
        this.price = price;
        this.category = category;
        this.guide = guide;
    }

    // Conversion from DTO
    public Trip(TripDTO tripDTO, Guide guide) {
        this.id = tripDTO.getId();
        this.startTime = tripDTO.getStartTime();
        this.endTime = tripDTO.getEndTime();
        this.startPosition = tripDTO.getStartPosition();
        this.name = tripDTO.getName();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();
        this.guide = guide;
    }

    // Methods to handle automatic timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id) &&
                Objects.equals(startTime, trip.startTime) &&
                Objects.equals(endTime, trip.endTime) &&
                Objects.equals(name, trip.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, name);
    }
}
