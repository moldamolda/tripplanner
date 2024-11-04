package dat.daos;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoctorDAOTest {
    private static Javalin app;
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final DoctorDAO doctorDao = new DoctorDAO(emf);
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DoctorDAOTest.class);

    private static DoctorDTO d1, d2, d3;

    @BeforeAll
    static void setupAll(){
        app = ApplicationConfig.startServer(7080, emf);
    }

    @BeforeEach
    void setup() throws ApiException {
        d1 = new DoctorDTO(1L,"Dr. Alice Smith", LocalDate.of(1975, 4, 12), 2000, "City Health Clinic", Speciality.FAMILY_MEDICINE);
        d2 = new DoctorDTO(2L,"Dr. Bob Johnson", LocalDate.of(1980, 8, 5), 2005, "Downtown Medical Center", Speciality.SURGERY);
        d3 = new DoctorDTO(3L,"Dr. Sophie Turner", LocalDate.of(1985, 6, 12), 2012, "West End Clinic", Speciality.PSYCHIATRY);

        doctorDao.create(d1);
        doctorDao.create(d2);
        doctorDao.create(d3);
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Doctor").executeUpdate();
            try {
                em.createNativeQuery("ALTER SEQUENCE doctor_doctor_id_seq RESTART WITH 1").executeUpdate();
            } catch (Exception ignored) {
                // Log a warning or ignore sequence reset errors if not critical
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error during teardown: {}", e.getMessage(), e);
        }
    }


    @AfterAll
    static void tearDownAll(){
        ApplicationConfig.stopServer(app);
    }

    @Test
    @Order(1)
    void read() throws ApiException {
        DoctorDTO readDoctor = doctorDao.read(d1.getId());
        assertEquals(d1.getId(), readDoctor.getId());
    }

    @Test
    @Order(2)
    void readAll() throws ApiException {
        List<DoctorDTO> allDoctors = doctorDao.readAll();
        assertEquals(3, allDoctors.size());
    }

    @Test
    @Order(3)
    void create() throws ApiException {
        DoctorDTO newDoctor = new DoctorDTO(null, "Dr. John Doe", LocalDate.of(1990, 1, 1), 2015, "New Clinic", Speciality.PEDIATRICS);
        DoctorDTO createdDoctor = doctorDao.create(newDoctor);

        // Read the doctor again from the database
        DoctorDTO actual = doctorDao.read(createdDoctor.getId());

        // Assert that the created doctor and the actual doctor are equal
        assertEquals(createdDoctor, actual);
    }


    @Test
    @Order(4)
    void delete() throws ApiException {
        doctorDao.delete(d1.getId());
        List<DoctorDTO> allDoctors = doctorDao.readAll();
        assertEquals(2, allDoctors.size());
    }

    @Test
    @Order(5)
    void update() throws ApiException {
        d1.setName("Dr. Alice Updated");
        doctorDao.update(d1.getId(), d1);
        DoctorDTO updated = doctorDao.read(d1.getId());
        assertEquals("Dr. Alice Updated", updated.getName());
    }
    @Test
    @Order(6)
    void doctorBySpeciality() throws ApiException {
        List<DoctorDTO> familyMedicineDoctors = doctorDao.doctorBySpeciality(Speciality.FAMILY_MEDICINE);
        assertEquals(1, familyMedicineDoctors.size());
        assertEquals(d1.getId(), familyMedicineDoctors.get(0).getId());

        List<DoctorDTO> surgeryDoctors = doctorDao.doctorBySpeciality(Speciality.SURGERY);
        assertEquals(1, surgeryDoctors.size());
        assertEquals(d2.getId(), surgeryDoctors.get(0).getId());
    }

    @Test
    @Order(7)
    void doctorByBirthdateRange() throws ApiException {
        LocalDate fromDate = LocalDate.of(1970, 1, 1);
        LocalDate toDate = LocalDate.of(1980, 12, 31);

        List<DoctorDTO> doctorsInRange = doctorDao.doctorByBirthdateRange(fromDate, toDate);
        assertEquals(2, doctorsInRange.size());
        assertTrue(doctorsInRange.stream().anyMatch(doctor -> doctor.getId().equals(d1.getId())));
        assertTrue(doctorsInRange.stream().anyMatch(doctor -> doctor.getId().equals(d2.getId())));
    }
}