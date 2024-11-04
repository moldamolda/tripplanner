package dat.controllers.impl;
/*
import dat.controllers.IController;
import dat.daos.DoctorMockDAO;
import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DoctorMockController implements IController<DoctorDTO, Integer> {

    private final DoctorMockDAO dao = new DoctorMockDAO();


    @Override
    public void read(Context ctx) throws ApiException {
        try {
            Long id = Long.valueOf(ctx.pathParam("id"));
            DoctorDTO doctorDTO = dao.read(id);

            if (doctorDTO == null) {
                throw new ApiException(404, "Doctor not found - /api/doctors/" + id);
            }

            ctx.status(200).json(doctorDTO);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format");
        } catch (Exception e) {
            throw new ApiException(500, "Internal server error");
        }
    }
        public void readSpeciality (Context ctx) throws ApiException {
            String specialityParam = ctx.pathParam("speciality").toUpperCase();

            try {
                Speciality speciality = Speciality.valueOf(specialityParam);
                List<DoctorDTO> doctorsBySpeciality = dao.doctorBySpeciality(speciality);

                if (doctorsBySpeciality.isEmpty()) {
                    throw new ApiException(404, "No doctors found for the given speciality");
                } else {
                    ctx.status(200).json(doctorsBySpeciality);
                }

            } catch (IllegalArgumentException e) {
                throw new ApiException(400, "Invalid speciality");
            } catch (Exception e) {
                throw new ApiException(500, "Internal server error");
            }
        }

        public void readBirthRange (Context ctx) throws ApiException {
            String fromDateParam = ctx.queryParam("from");
            String toDateParam = ctx.queryParam("to");

            try {
                if (fromDateParam == null || toDateParam == null) {
                    throw new ApiException(400, "Both 'from' and 'to' date parameters are required.");
                }

                LocalDate fromDate = LocalDate.parse(fromDateParam);
                LocalDate toDate = LocalDate.parse(toDateParam);

                List<DoctorDTO> doctorsInRange = dao.doctorByBirthdateRange(fromDate, toDate);

                if (doctorsInRange.isEmpty()) {
                    throw new ApiException(404, "No doctors found in the given date range");
                } else {
                    ctx.status(200).json(doctorsInRange);
                }

            } catch (DateTimeParseException e) {
                throw new ApiException(400, "Invalid date format. Please use yyyy-MM-dd.");
            } catch (Exception e) {
                throw new ApiException(500, "Internal server error");
            }
        }


        @Override
        public void readAll (Context ctx){
            List<DoctorDTO> doctorDTOS = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(doctorDTOS);
        }

        @Override
        public void create (Context ctx) throws ApiException {
            try {
                DoctorDTO jsonRequest = ctx.bodyAsClass(DoctorDTO.class);
                DoctorDTO doctorDTO = dao.create(jsonRequest);
                ctx.status(201).json(doctorDTO);
            } catch (Exception e) {
                throw new ApiException(500, "Internal server error");
            }
        }

        @Override
        public void update (Context ctx){
            Long id = Long.valueOf(ctx.pathParamAsClass("id", Integer.class).get());
            DoctorDTO doctorDTO = dao.update(id, ctx.bodyAsClass(DoctorDTO.class));
            ctx.res().setStatus(200);
            ctx.json(doctorDTO);
        }

        @Override
        public void delete (Context ctx){
            Long id = Long.valueOf(ctx.pathParamAsClass("id", Integer.class).get());
            dao.delete(id);
            ctx.res().setStatus(204);
        }
    }


 */