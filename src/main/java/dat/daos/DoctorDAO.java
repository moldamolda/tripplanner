package dat.daos;

import dat.dtos.DoctorDTO;
import dat.entities.Doctor;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class DoctorDAO implements IDAO<DoctorDTO, Long> {
    private final Logger log = LoggerFactory.getLogger(DoctorDAO.class);
    private final EntityManagerFactory emf;

    public DoctorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public DoctorDTO read(Long id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Doctor doctor = em.find(Doctor.class, id);
            if (doctor == null) {
                throw new ApiException(404, "Doctor not found with id " + id);
            }
            return new DoctorDTO(doctor);
        }
    }

    @Override
    public List<DoctorDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery("SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d", DoctorDTO.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Error reading all doctors: {}", e.getMessage(), e);
            throw new ApiException(400, "Something went wrong during readAll");
        }
    }

    @Override
    public DoctorDTO create(DoctorDTO doctorDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor doctor = new Doctor(doctorDTO);
            em.persist(doctor);
            em.getTransaction().commit();
            return new DoctorDTO(doctor);
        } catch (PersistenceException e) {
            log.error("Error creating doctor: {}", e.getMessage(), e);
            throw new ApiException(400, "Doctor already exists or something else went wrong");
        }
    }

    @Override
    public DoctorDTO update(Long id, DoctorDTO doctorDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor existingDoctor = em.find(Doctor.class, id);
            if (existingDoctor == null) {
                throw new ApiException(404, "Doctor with that id is not found");
            }
            existingDoctor.setName(doctorDTO.getName());
            existingDoctor.setDateOfBirth(doctorDTO.getDateOfBirth());
            existingDoctor.setYearOfGraduation(doctorDTO.getYearOfGraduation());
            existingDoctor.setNameOfClinic(doctorDTO.getNameOfClinic());
            existingDoctor.setSpeciality(doctorDTO.getSpeciality());
            em.getTransaction().commit();
            return new DoctorDTO(existingDoctor);
        } catch (PersistenceException e) {
            log.error("Error updating doctor with id {}: {}", id, e.getMessage(), e);
            throw new ApiException(400, "Doctor not found or something else went wrong during update");
        }
    }

    @Override
    public void delete(Long id) throws ApiException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Doctor doctor = em.find(Doctor.class, id);
            if (doctor == null) {
                throw new ApiException(404, "Doctor not found with id " + id);
            }
            em.remove(doctor);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            log.error("Error deleting doctor with id {}: {}", id, e.getMessage(), e);
            throw new ApiException(500, "An error occurred while deleting the doctor");
        } finally {
            em.close();
        }
    }

    public List<DoctorDTO> doctorBySpeciality(Speciality speciality) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery("SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d WHERE d.speciality = :speciality", DoctorDTO.class);
            query.setParameter("speciality", speciality);
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Error retrieving doctors by speciality {}: {}", speciality, e.getMessage(), e);
            throw new ApiException(400, "Something went wrong while retrieving doctors by speciality");
        }
    }

    public List<DoctorDTO> doctorByBirthdateRange(LocalDate from, LocalDate to) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery("SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d WHERE d.dateOfBirth BETWEEN :from AND :to", DoctorDTO.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            return query.getResultList();
        } catch (PersistenceException e) {
            log.error("Error retrieving doctors by birthdate range {} to {}: {}", from, to, e.getMessage(), e);
            throw new ApiException(400, "Something went wrong while retrieving doctors by birthdate range");
        }
    }
}
