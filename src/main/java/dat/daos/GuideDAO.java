package dat.daos;

import dat.dtos.GuideDTO;
import dat.entities.Guide;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GuideDAO implements IDAO<GuideDTO, Long> {
    private final Logger log = LoggerFactory.getLogger(GuideDAO.class);
    private final EntityManagerFactory emf;

    public GuideDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public GuideDTO create(GuideDTO guideDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = new Guide(guideDTO);
            em.persist(guide);
            em.getTransaction().commit();
            return new GuideDTO(guide);
        } catch (PersistenceException e) {
            log.error("Error creating guide: {}", e.getMessage(), e);
            throw new ApiException(400, "Guide already exists or something else went wrong");
        }
    }

    @Override
    public GuideDTO read(Long id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = em.find(Guide.class, id);
            if (guide == null) {
                throw new ApiException(404, "Guide not found with id " + id);
            }
            return new GuideDTO(guide);
        }
    }

    @Override
    public List<GuideDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<GuideDTO> query = em.createQuery("SELECT new dat.dtos.GuideDTO(g) FROM Guide g", GuideDTO.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Error reading all guides: {}", e.getMessage(), e);
            throw new ApiException(400, "Something went wrong during readAll");
        }
    }

    @Override
    public GuideDTO update(Long id, GuideDTO guideDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide existingGuide = em.find(Guide.class, id);
            if (existingGuide == null) {
                throw new ApiException(404, "Guide not found with id " + id);
            }
            existingGuide.setFirstName(guideDTO.getFirstName());
            existingGuide.setLastName(guideDTO.getLastName());
            existingGuide.setEmail(guideDTO.getEmail());
            existingGuide.setPhone(guideDTO.getPhone());
            existingGuide.setYearsOfExperience(guideDTO.getYearsOfExperience());
            em.getTransaction().commit();
            return new GuideDTO(existingGuide);
        } catch (PersistenceException e) {
            log.error("Error updating guide with id {}: {}", id, e.getMessage(), e);
            throw new ApiException(400, "Guide not found or something else went wrong during update");
        }
    }

    @Override
    public void delete(Long id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, id);
            if (guide == null) {
                throw new ApiException(404, "Guide not found with id " + id);
            }
            em.remove(guide);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            log.error("Error deleting guide with id {}: {}", id, e.getMessage(), e);
            throw new ApiException(500, "An error occurred while deleting the guide");
        }
    }
}
