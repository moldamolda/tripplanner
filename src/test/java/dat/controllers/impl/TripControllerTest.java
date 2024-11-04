package dat.controllers.impl;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.enums.TripCategory;
import dat.exceptions.ApiException;
import io.javalin.Javalin;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TripControllerTest {

    private static Javalin app;
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final String BASE_URL = "http://localhost:7070/api/trips";

    @BeforeAll
    static void init() {
        app = ApplicationConfig.startServer(7070, emf);
    }

    @BeforeEach
    void setUp() throws ApiException {
        // Populate test data for trips and guides
        List<GuideDTO> guides = Populator.populateGuides(emf);
        System.out.println("Populated Guides: " + guides);

        // Populate trips using the populated guides
        List<TripDTO> trips = Populator.populateTrips(emf, guides);
        System.out.println("Populated Trips: " + trips);
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // Delete Trip entities first, then Guide entities
            em.createQuery("DELETE FROM Trip").executeUpdate();
            em.createQuery("DELETE FROM Guide").executeUpdate();
            // Resetting the sequence for Trip and Guide entities
            em.createNativeQuery("ALTER SEQUENCE trip_trip_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE guide_guide_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void closeDown() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void testReadAll() {
        List<TripDTO> trips =
                given()
                        .when()
                        .get(BASE_URL)
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<TripDTO>>() {
                        });

        assertNotNull(trips, "The list of trips should not be null");
        assertThat("The number of trips should be greater than zero", trips.size(), greaterThan(0));
    }

    @Test
    void testReadById() {
        TripDTO trip =
                given()
                        .when()
                        .get(BASE_URL + "/1")
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(TripDTO.class);

        assertNotNull(trip, "The trip should not be null");
        assertThat("The trip ID should match the requested ID", trip.getId(), is(1L));
        assertThat("The trip name should not be empty", trip.getName(), not(emptyOrNullString()));
    }

    @Test
    void testCreateTrip() {
        // Assuming there's a valid guide with ID 1 to associate with the new trip
        Long validGuideId = 1L;

        TripDTO newTrip = new TripDTO(null, LocalDateTime.now(), LocalDateTime.now().plusHours(4), "Test Start", "Test Trip", 150.0, TripCategory.CITY, validGuideId);

        TripDTO createdTrip =
                given()
                        .contentType("application/json")
                        .body(newTrip)
                        .when()
                        .post(BASE_URL)
                        .then()
                        .statusCode(201) // Corrected to the proper status code for creation
                        .log().all()
                        .extract()
                        .as(TripDTO.class);

        assertNotNull(createdTrip.getId(), "The created trip ID should not be null");
        assertThat("The created trip name should match the provided name", createdTrip.getName(), is("Test Trip"));
        assertThat("The created trip price should match the provided price", createdTrip.getPrice(), is(150.0));
    }


    @Test
    void testUpdateTrip() {
        TripDTO updatedTrip = new TripDTO(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(3), "Updated Start", "Updated Trip", 200.0, TripCategory.FOREST, null);

        TripDTO resultTrip =
                given()
                        .contentType("application/json")
                        .body(updatedTrip)
                        .when()
                        .put(BASE_URL + "/1")
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(TripDTO.class);

        assertNotNull(resultTrip, "The updated trip should not be null");
        assertThat("The updated trip name should match the provided name", resultTrip.getName(), is("Updated Trip"));
        assertThat("The updated trip category should match the provided category", resultTrip.getCategory(), is(TripCategory.FOREST));
    }

    @Test
    void testDeleteTrip() {
        // Create a trip to delete
        TripDTO newTrip = new TripDTO(null, LocalDateTime.now(), LocalDateTime.now().plusHours(4), "Test Start", "Test Trip", 150.0, TripCategory.CITY, 1L);

        TripDTO createdTrip =
                given()
                        .contentType("application/json")
                        .body(newTrip)
                        .when()
                        .post(BASE_URL)
                        .then()
                        .statusCode(201)
                        .log().all()
                        .extract()
                        .as(TripDTO.class);

        // Delete the created trip
        given()
                .when()
                .delete(BASE_URL + "/" + createdTrip.getId())
                .then()
                .statusCode(204)
                .log().all();

        // Verify the trip no longer exists
        given()
                .when()
                .get(BASE_URL + "/" + createdTrip.getId())
                .then()
                .statusCode(404)
                .log().all();
    }


    @Test
    void testAddGuideToTrip() {
        given()
                .when()
                .put(BASE_URL + "/1/guides/1")
                .then()
                .statusCode(200)
                .log().all();

        // Verify if the guide has been associated with the trip
        TripDTO trip =
                given()
                        .when()
                        .get(BASE_URL + "/1")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(TripDTO.class);

        assertNotNull(trip.getGuideId(), "The guide ID should be associated with the trip");
        assertThat("The guide ID should match the expected value", trip.getGuideId(), is(1L));
    }

    @Test
    void testGetTripsByGuide() {
        List<TripDTO> tripsByGuide =
                given()
                        .when()
                        .get(BASE_URL + "/guide/1")
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<TripDTO>>() {
                        });

        assertNotNull(tripsByGuide, "The trips by guide list should not be null");
        assertThat("The trips by guide list should not be empty", tripsByGuide.size(), greaterThan(0));

        // Additional check to verify that the guideId is set for the retrieved trips
        for (TripDTO trip : tripsByGuide) {
            assertNotNull(trip.getGuideId(), "The guide ID should be associated with the trip");
            assertThat("The guide ID should match the expected value", trip.getGuideId(), is(1L));
        }
    }
}