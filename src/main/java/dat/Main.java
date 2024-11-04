package dat;



import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.routes.Routes;
import dat.routes.Routes;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import jakarta.persistence.EntityManagerFactory;

public class Main {
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("doctor");

    public static void main(String[] args) {
        ApplicationConfig.startServer(7070,emf);

    }
}