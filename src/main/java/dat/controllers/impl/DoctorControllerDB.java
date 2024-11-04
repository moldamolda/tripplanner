package dat.controllers.impl;

import dat.controllers.IController;
import dat.dtos.DoctorDTO;
import dat.daos.DoctorDAO;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DoctorControllerDB implements IController<DoctorDTO, Integer> {
    private final Logger log = LoggerFactory.getLogger(DoctorControllerDB.class);
    private final DoctorDAO dao;

    public DoctorControllerDB(DoctorDAO dao) {
        this.dao = dao;
    }

    @Override
    public void readAll(Context ctx) throws ApiException {
        List<DoctorDTO> doctorDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(doctorDTOS, DoctorDTO.class);
        log.info("Successfully retrieved all doctors");
    }

    @Override
    public void read(Context ctx) throws ApiException {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            DoctorDTO doctorDTO = dao.read(id);
            ctx.res().setStatus(200);
            ctx.json(doctorDTO, DoctorDTO.class);
            log.info("Successfully retrieved doctor with id {}", id);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format: id must be a number");
        }
    }

    public void readSpeciality(Context ctx) throws ApiException {
        String specialityParam = ctx.pathParam("speciality").toUpperCase();
        try {
            Speciality speciality = Speciality.valueOf(specialityParam);
            List<DoctorDTO> doctorsBySpeciality = dao.doctorBySpeciality(speciality);
            if (doctorsBySpeciality.isEmpty()) {
                throw new ApiException(404, "No doctors found for the given speciality");
            }
            ctx.res().setStatus(200);
            ctx.json(doctorsBySpeciality, DoctorDTO.class);
            log.info("Successfully retrieved doctors for speciality {}", speciality);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid speciality format");
        }
    }

    public void readBirthRange(Context ctx) throws ApiException {
        String fromDateParam = ctx.queryParam("from");
        String toDateParam = ctx.queryParam("to");

        if (fromDateParam == null || toDateParam == null) {
            throw new ApiException(400, "Both 'from' and 'to' date parameters are required.");
        }

        try {
            LocalDate fromDate = LocalDate.parse(fromDateParam);
            LocalDate toDate = LocalDate.parse(toDateParam);
            List<DoctorDTO> doctorsInRange = dao.doctorByBirthdateRange(fromDate, toDate);
            if (doctorsInRange.isEmpty()) {
                throw new ApiException(404, "No doctors found in the given date range");
            }
            ctx.res().setStatus(200);
            ctx.json(doctorsInRange, DoctorDTO.class);
            log.info("Successfully retrieved doctors in the date range {} to {}", fromDate, toDate);
        } catch (DateTimeParseException e) {
            throw new ApiException(400, "Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    @Override
    public void create(Context ctx) throws ApiException {
        DoctorDTO jsonRequest = ctx.bodyAsClass(DoctorDTO.class);
        DoctorDTO doctorDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(doctorDTO, DoctorDTO.class);
        log.info("Successfully created a new doctor");
    }

    @Override
    public void update(Context ctx) throws ApiException {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            DoctorDTO updatedDoctor = ctx.bodyAsClass(DoctorDTO.class);
            DoctorDTO doctorDTO = dao.update(id, updatedDoctor);
            ctx.res().setStatus(200);
            ctx.json(doctorDTO, DoctorDTO.class);
            log.info("Successfully updated doctor with id {}", id);
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
            log.info("Successfully deleted doctor with id {}", id);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format: id must be a number");
        }
    }
}
