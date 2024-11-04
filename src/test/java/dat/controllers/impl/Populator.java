package dat.controllers.impl;

import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.TripCategory;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Populator {

    public static List<GuideDTO> populateGuides(EntityManagerFactory emf) {
        List<GuideDTO> guideDTOList = new ArrayList<>();

        Guide guide1 = new Guide("John", "Doe", "john.doe@example.com", "1234567890", 10);
        Guide guide2 = new Guide("Jane", "Smith", "jane.smith@example.com", "0987654321", 5);
        Guide guide3 = new Guide("Mike", "Johnson", "mike.johnson@example.com", "1122334455", 15);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);
            em.getTransaction().commit();

            guideDTOList.add(new GuideDTO(guide1));
            guideDTOList.add(new GuideDTO(guide2));
            guideDTOList.add(new GuideDTO(guide3));

            System.out.println("Guides have been committed successfully.");
        } catch (Exception e) {
            System.err.println("Error during guide population: " + e.getMessage());
        }

        return guideDTOList;
    }

    public static List<TripDTO> populateTrips(EntityManagerFactory emf, List<GuideDTO> guides) throws ApiException {
        List<TripDTO> tripDTOList = new ArrayList<>();

        // Ensure there are guides to associate with trips
        if (guides.isEmpty()) {
            throw new ApiException(500, "No guides found for trip population");
        }

        Guide guide1 = new Guide(guides.get(0));
        Guide guide2 = new Guide(guides.get(1));
        Guide guide3 = new Guide(guides.get(2));

        Trip trip1 = new Trip(LocalDateTime.of(2024, 5, 10, 8, 30),
                LocalDateTime.of(2024, 5, 10, 12, 30),
                "Central Park", "City Tour", 100.0, TripCategory.CITY, guide1);

        Trip trip2 = new Trip(LocalDateTime.of(2024, 6, 15, 9, 0),
                LocalDateTime.of(2024, 6, 15, 13, 0),
                "Beachside", "Beach Adventure", 150.0, TripCategory.BEACH, guide2);

        Trip trip3 = new Trip(LocalDateTime.of(2024, 7, 20, 10, 0),
                LocalDateTime.of(2024, 7, 20, 14, 0),
                "Mountain Base", "Mountain Trek", 200.0, TripCategory.FOREST, guide3);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.getTransaction().commit();

            tripDTOList.add(new TripDTO(trip1));
            tripDTOList.add(new TripDTO(trip2));
            tripDTOList.add(new TripDTO(trip3));

            System.out.println("Trips have been committed successfully.");
        } catch (Exception e) {
            System.err.println("Error during trip population: " + e.getMessage());
            throw new ApiException(500, "Error populating trips: " + e.getMessage());
        }

        return tripDTOList;
    }
}