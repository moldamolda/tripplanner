package dat.config;

import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.TripCategory;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {
        // Initialize EntityManagerFactory from the Hibernate configuration
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("trip");

        // Create sample data for guides with trips
        Guide guide1 = createGuideWithTrips("John", "Doe", "john.doe@example.com", "1234567890", 10,
                LocalDateTime.of(2024, 5, 10, 8, 30), LocalDateTime.of(2024, 5, 10, 12, 30), "Central Park Tour", 50.0, TripCategory.CITY);
        Guide guide2 = createGuideWithTrips("Jane", "Smith", "jane.smith@example.com", "0987654321", 8,
                LocalDateTime.of(2024, 6, 15, 9, 0), LocalDateTime.of(2024, 6, 15, 13, 0), "Beach Adventure", 80.0, TripCategory.BEACH);

        // Persist the created guides with their trips
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(guide1);
            em.persist(guide2);
            em.getTransaction().commit();
        }
    }

    // Method to create a guide and link it to trips
    private static Guide createGuideWithTrips(String firstName, String lastName, String email, String phone, int yearsOfExperience,
                                              LocalDateTime startTime, LocalDateTime endTime, String tripName, double price, TripCategory category) {
        Guide guide = new Guide(firstName, lastName, email, phone, yearsOfExperience);
        Set<Trip> trips = getTripsForGuide(guide, startTime, endTime, tripName, price, category);
        guide.setTrips(trips);  // Link trips to guide
        return guide;
    }

    // Generate a set with one trip for a given guide
    private static Set<Trip> getTripsForGuide(Guide guide, LocalDateTime startTime, LocalDateTime endTime, String tripName, double price, TripCategory category) {
        Trip trip = new Trip(startTime, endTime, "Default Start Position", tripName, price, category, guide);

        // Use a Set to maintain the trip collection (even though there's only one)
        Set<Trip> trips = new HashSet<>();
        trips.add(trip);

        return trips;
    }
}
