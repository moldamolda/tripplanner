package dat.routes;

import dat.controllers.impl.TripController;
import dat.daos.TripDAO;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoutes {
    private final TripController tripController;

    public TripRoutes(EntityManagerFactory emf) {
        tripController = new TripController(new TripDAO(emf));
    }

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", tripController::readAll);
            get("/{id}", tripController::read, Role.USER);
            post("/", tripController::create, Role.SUPERMAN);
            put("/{id}", tripController::update, Role.USER);
            delete("/{id}", tripController::delete, Role.USER);
            put("/{tripId}/guides/{guideId}", tripController::addGuideToTrip, Role.USER);
            get("/guide/{guideId}", tripController::getTripsByGuide, Role.USER);
            get("/guides/totalprice", tripController::getTotalPriceByGuide, Role.USER);
        };
    }
}
