package dat.daos;

import dat.dtos.GuideTotalPriceDTO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.TripCategory;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dat.dtos.GuideTotalPriceDTO;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TripDAO implements IDAO<TripDTO, Long>, ITripGuideDAO {
    private final Logger log = LoggerFactory.getLogger(TripDAO.class);
    private final EntityManagerFactory emf;

    public TripDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public TripDTO create(TripDTO tripDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Fetch the Guide entity if a guideId is provided
            Guide guide = null;
            if (tripDTO.getGuideId() != null) {
                guide = em.find(Guide.class, tripDTO.getGuideId());
                if (guide == null) {
                    throw new ApiException(404, "Guide not found with id " + tripDTO.getGuideId());
                }
            }

            // Create a new Trip entity with the fetched Guide
            Trip trip = new Trip(tripDTO, guide);
            em.persist(trip);
            em.getTransaction().commit();

            return new TripDTO(trip);
        } catch (PersistenceException e) {
            log.error("Error creating trip: {}", e.getMessage(), e);
            throw new ApiException(400, "Something went wrong during trip creation");
        }
    }



    @Override
    public TripDTO read(Long id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Trip> query = em.createQuery(
                    "SELECT t FROM Trip t LEFT JOIN FETCH t.guide WHERE t.id = :id", Trip.class);
            query.setParameter("id", id);
            Trip trip = query.getSingleResult();
            if (trip == null) {
                throw new ApiException(404, "Trip not found with id " + id);
            }
            return new TripDTO(trip); // This will include guideId
        } catch (PersistenceException e) {
            log.error("Error reading trip with id {}: {}", id, e.getMessage(), e);
            throw new ApiException(400, "Something went wrong while fetching the trip");
        }
    }



    @Override
    public List<TripDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            // Fetch all trips with their associated guide information
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t LEFT JOIN FETCH t.guide", Trip.class);
            List<Trip> trips = query.getResultList();
            return trips.stream().map(TripDTO::new).collect(Collectors.toList());
        } catch (PersistenceException e) {
            log.error("Error reading all trips: {}", e.getMessage(), e);
            throw new ApiException(400, "Something went wrong during readAll");
        }
    }


    @Override
    public TripDTO update(Long id, TripDTO tripDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip existingTrip = em.find(Trip.class, id);
            if (existingTrip == null) {
                throw new ApiException(404, "Trip with that id is not found");
            }
            existingTrip.setStartTime(tripDTO.getStartTime());
            existingTrip.setEndTime(tripDTO.getEndTime());
            existingTrip.setStartPosition(tripDTO.getStartPosition());
            existingTrip.setName(tripDTO.getName());
            existingTrip.setPrice(tripDTO.getPrice());
            existingTrip.setCategory(tripDTO.getCategory());
            em.getTransaction().commit();
            return new TripDTO(existingTrip);
        } catch (PersistenceException e) {
            log.error("Error updating trip with id {}: {}", id, e.getMessage(), e);
            throw new ApiException(400, "Trip not found or something else went wrong during update");
        }
    }

    @Override
    public void delete(Long id) throws ApiException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "Trip not found with id " + id);
            }
            em.remove(trip);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.error("Error deleting trip with id {}: {}", id, e.getMessage(), e);
            throw new ApiException(500, "An error occurred while deleting the trip");
        } finally {
            em.close();
        }
    }

    @Override
    public void addGuideToTrip(int tripId, int guideId) throws ApiException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, (long) tripId);
            Guide guide = em.find(Guide.class, (long) guideId);
            if (trip == null) {
                throw new ApiException(404, "Trip not found with id " + tripId);
            }
            if (guide == null) {
                throw new ApiException(404, "Guide not found with id " + guideId);
            }
            trip.setGuide(guide);
            em.merge(trip);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.error("Error adding guide with id {} to trip with id {}: {}", guideId, tripId, e.getMessage(), e);
            throw new ApiException(500, "An error occurred while adding guide to trip");
        } finally {
            em.close();
        }
    }

    @Override
    public Set<TripDTO> getTripsByGuide(int guideId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            // Use JPQL to fetch trips with their associated guide information
            TypedQuery<Trip> query = em.createQuery(
                    "SELECT t FROM Trip t JOIN FETCH t.guide WHERE t.guide.id = :guideId", Trip.class
            );
            query.setParameter("guideId", (long) guideId);

            List<Trip> trips = query.getResultList();
            if (trips.isEmpty()) {
                throw new ApiException(404, "No trips found for guide with id " + guideId);
            }

            // Map each Trip entity to a TripDTO, which will include the GuideDTO due to the constructor logic
            return trips.stream().map(TripDTO::new).collect(Collectors.toSet());
        } catch (PersistenceException e) {
            log.error("Error retrieving trips by guide with id {}: {}", guideId, e.getMessage(), e);
            throw new ApiException(400, "Something went wrong while retrieving trips by guide");
        }
    }


    public List<GuideTotalPriceDTO> getTotalPriceByGuide() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT g.id, SUM(t.price) FROM Guide g LEFT JOIN Trip t ON g.id = t.guide.id GROUP BY g.id", Object[].class);
            List<Object[]> results = query.getResultList();

            return results.stream()
                    .map(result -> new GuideTotalPriceDTO((Long) result[0], (Double) result[1]))
                    .collect(Collectors.toList());
        } catch (PersistenceException e) {
            log.error("Error retrieving total prices by guide: {}", e.getMessage());
            throw new ApiException(400, "Something went wrong while retrieving total prices by guide");
        }
    }



}
