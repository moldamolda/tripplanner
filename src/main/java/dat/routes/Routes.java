package dat.routes;

import dat.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final DoctorRoute doctorRoute;
    private final SecurityRoutes authRoute = new SecurityRoutes();

    public Routes(EntityManagerFactory emf) {
        doctorRoute = new DoctorRoute(emf);
    }
    public EndpointGroup getRoutes() {
        return () -> {
                path("/doctors", doctorRoute.getRoutes());
                path("/auth", authRoute.getSecurityRoutes());

        };
    }
}
