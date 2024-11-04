package dat.controllers.impl;

import dat.controllers.IController;
import dat.dtos.GuideTotalPriceDTO;
import dat.dtos.TripDTO;
import dat.daos.TripDAO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

public class TripController implements IController<TripDTO, Integer> {
    private final Logger log = LoggerFactory.getLogger(TripController.class);
    private final TripDAO dao;

    public TripController(TripDAO dao) {
        this.dao = dao;
    }

    @Override
    public void read(Context ctx) throws ApiException {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            TripDTO tripDTO = dao.read(id); // This now includes the guide details
            ctx.res().setStatus(200);
            ctx.json(tripDTO);
            log.info("Successfully retrieved trip with id {}", id);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format: id must be a number");
        }
    }

    @Override
    public void readAll(Context ctx) throws ApiException {
        List<TripDTO> tripDTOS = dao.readAll(); // This will include all trip details with guide information
        ctx.res().setStatus(200);
        ctx.json(tripDTOS);
        log.info("Successfully retrieved all trips");
    }


    @Override
    public void create(Context ctx) throws ApiException {
        TripDTO jsonRequest = ctx.bodyAsClass(TripDTO.class);
        TripDTO tripDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(tripDTO);
        log.info("Successfully created a new trip");
    }

    @Override
    public void update(Context ctx) throws ApiException {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            TripDTO updatedTrip = ctx.bodyAsClass(TripDTO.class);
            TripDTO tripDTO = dao.update(id, updatedTrip);
            ctx.res().setStatus(200);
            ctx.json(tripDTO);
            log.info("Successfully updated trip with id {}", id);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format: id must be a number");
        }
    }

    @Override
    public void delete(Context ctx) throws ApiException {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            dao.delete(id);
            ctx.res().setStatus(204);
            log.info("Successfully deleted trip with id {}", id);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format: id must be a number");
        }
    }

    public void addGuideToTrip(Context ctx) throws ApiException {
        try {
            int tripId = Integer.parseInt(ctx.pathParam("tripId"));
            int guideId = Integer.parseInt(ctx.pathParam("guideId"));
            dao.addGuideToTrip(tripId, guideId);
            ctx.res().setStatus(200);
            log.info("Successfully added guide with id {} to trip with id {}", guideId, tripId);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format: id must be a number");
        }
    }

    public void getTripsByGuide(Context ctx) throws ApiException {
        try {
            int guideId = Integer.parseInt(ctx.pathParam("guideId"));
            List<TripDTO> tripsByGuide = new ArrayList<>(dao.getTripsByGuide(guideId));
            ctx.res().setStatus(200);
            ctx.json(tripsByGuide);
            log.info("Successfully retrieved trips for guide with id {}", guideId);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format: id must be a number");
        }

    }

    public List<GuideTotalPriceDTO> getTotalPriceByGuide(Context ctx) throws ApiException {
        List<GuideTotalPriceDTO> guideTotalPrices = dao.getTotalPriceByGuide();
        ctx.res().setStatus(200);
        ctx.json(guideTotalPrices);
        log.info("Successfully retrieved total price of trips for each guide");
        return guideTotalPrices;
    }


}
