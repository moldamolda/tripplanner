package dat.routes;

import dat.controllers.impl.DoctorControllerDB;
import dat.daos.DoctorDAO;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class DoctorRoute {

    //private static final DoctorControllerDB doctorMockController = new DoctorControllerDB();
    //why static?
    private DoctorControllerDB doctorControllerDB;

    public DoctorRoute(EntityManagerFactory emf) {
        doctorControllerDB = new DoctorControllerDB(new DoctorDAO(emf));

    }

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/", doctorControllerDB::readAll, Role.ANYONE);
            get("/{id}", doctorControllerDB::read, Role.ANYONE);
            get("/doctor/speciality/{speciality}", doctorControllerDB::readSpeciality, Role.ANYONE);
            get("/doctor/birthdate/range", doctorControllerDB::readBirthRange, Role.ANYONE);
            post("/", doctorControllerDB::create, Role.ANYONE);
            put("/{id}", doctorControllerDB::update, Role.ANYONE);
            delete("/{id}", doctorControllerDB::delete, Role.ANYONE);
        };
    }
}
