package dat.daos;

import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DoctorMockDAO implements IDAO<DoctorDTO, Long> {

    private static final List<DoctorDTO> doctors = new ArrayList<>();
    private static Long nextId = 1L;

    static {
        // Populate the list with initial mock data
        doctors.add(new DoctorDTO(1L, "Dr. Alice Smith", LocalDate.of(1975, 4, 12), 2000, "City Health Clinic", Speciality.FAMILY_MEDICINE));
        doctors.add(new DoctorDTO(2L, "Dr. Bob Johnson", LocalDate.of(1980, 8, 5), 2005, "Downtown Medical Center", Speciality.SURGERY));
        doctors.add(new DoctorDTO(3L, "Dr. Clara Lee", LocalDate.of(1983, 7, 22), 2008, "Green Valley Hospital", Speciality.PEDIATRICS));
        doctors.add(new DoctorDTO(4L, "Dr. David Park", LocalDate.of(1978, 11, 15), 2003, "Hillside Medical Practice", Speciality.PSYCHIATRY));
        doctors.add(new DoctorDTO(5L, "Dr. Emily White", LocalDate.of(1982, 9, 30), 2007, "Metro Health Center", Speciality.GERIATRICS));
        doctors.add(new DoctorDTO(6L, "Dr. Fiona Martinez", LocalDate.of(1985, 2, 17), 2010, "Riverside Wellness Clinic", Speciality.SURGERY));
        doctors.add(new DoctorDTO(7L, "Dr. George Kim", LocalDate.of(1979, 5, 29), 2004, "Summit Health Institute", Speciality.FAMILY_MEDICINE));
    }


    @Override
    public List<DoctorDTO> readAll() {
        return new ArrayList<>(doctors);
    }

    @Override
    public DoctorDTO read(Long id) {
        return doctors.stream()
                .filter(doctor -> doctor.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    public List<DoctorDTO> doctorBySpeciality(Speciality speciality) {
        return doctors.stream()
                .filter(doctor -> doctor.getSpeciality().equals(speciality))
                .collect(Collectors.toList());
    }

    public List<DoctorDTO> doctorByBirthdateRange(LocalDate from, LocalDate to) {
        return doctors.stream()
                .filter(doctor -> !doctor.getDateOfBirth().isBefore(from) && !doctor.getDateOfBirth().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDTO create(DoctorDTO doctor) {
        doctor.setId(nextId);
        doctors.add(doctor);
        nextId++;
        return doctor;
    }

    //update doctor by ID using streams

    @Override
    public DoctorDTO update(Long id, DoctorDTO updatedDoctor) {
        DoctorDTO existingDoctor = read(id);
        if (existingDoctor != null) {
            existingDoctor.setName(updatedDoctor.getName());
            existingDoctor.setSpeciality(updatedDoctor.getSpeciality());
            existingDoctor.setDateOfBirth(updatedDoctor.getDateOfBirth());
            existingDoctor.setYearOfGraduation(updatedDoctor.getYearOfGraduation());
            existingDoctor.setNameOfClinic(updatedDoctor.getNameOfClinic());
            return existingDoctor;
        }
        return null;  // Doctor not found
    }

    @Override
    public void delete(Long id) {
        doctors.removeIf(doctor -> doctor.getId().equals(id));
    }
}

