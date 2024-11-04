package dat.config;

import dat.entities.Appointment;
import dat.entities.Doctor;
import dat.enums.Speciality;
import dat.security.entities.User;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {
        // Initialize EntityManagerFactory from the Hibernate configuration
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("doctor");

        // Create sample data for doctors with one appointment each
        Doctor doctor1 = createDoctorWithAppointment("Dr. Alice Smith", LocalDate.of(1975, 4, 12), 2000, "City Health Clinic", Speciality.FAMILY_MEDICINE, "John Smith", LocalDate.of(2023, 11, 24), LocalTime.of(9, 45), "First visit");
        Doctor doctor2 = createDoctorWithAppointment("Dr. Bob Johnson", LocalDate.of(1980, 8, 5), 2005, "Downtown Medical Center", Speciality.SURGERY, "Alice Johnson", LocalDate.of(2023, 11, 27), LocalTime.of(10, 30), "Follow up");
        Doctor doctor3 = createDoctorWithAppointment("Dr. Clara Lee", LocalDate.of(1983, 7, 22), 2008, "Green Valley Hospital", Speciality.PEDIATRICS, "Bob Anderson", LocalDate.of(2023, 12, 12), LocalTime.of(14, 0), "General check");
        Doctor doctor4 = createDoctorWithAppointment("Dr. David Park", LocalDate.of(1978, 11, 15), 2003, "Hillside Medical Practice", Speciality.PSYCHIATRY, "Emily White", LocalDate.of(2023, 12, 15), LocalTime.of(11, 0), "Consultation");
        Doctor doctor5 = createDoctorWithAppointment("Dr. Fiona Martinez", LocalDate.of(1985, 2, 17), 2010, "Riverside Wellness Clinic", Speciality.SURGERY, "David Martinez", LocalDate.of(2023, 12, 18), LocalTime.of(15, 30), "Routine checkup");
        Doctor doctor6 = createDoctorWithAppointment("Dr. George Kim", LocalDate.of(1979, 5, 29), 2004, "Summit Health Institute", Speciality.FAMILY_MEDICINE, "Clara Lee", LocalDate.of(2023, 12, 20), LocalTime.of(8, 45), "Vaccine shot");


        // Persist the created doctors with their appointments
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(doctor1);
            em.persist(doctor2);
            em.persist(doctor3);
            em.persist(doctor4);
            em.persist(doctor5);
            em.persist(doctor6);
            em.getTransaction().commit();
        }
    }

    // Method to create a doctor and link it to one appointment
    private static Doctor createDoctorWithAppointment(String name, LocalDate dateOfBirth, int yearOfGraduation, String clinicName, Speciality speciality, String clientName, LocalDate appointmentDate, LocalTime appointmentTime, String comment) {
        Doctor doctor = new Doctor(name, dateOfBirth, yearOfGraduation, clinicName, speciality);
        Set<Appointment> appointments = getAppointmentsForDoctor(doctor, clientName, appointmentDate, appointmentTime, comment);
        doctor.setAppointments(appointments);  // Link appointment to doctor
        return doctor;
    }

    // Generate a set with one appointment for a given doctor
    private static Set<Appointment> getAppointmentsForDoctor(Doctor doctor, String clientName, LocalDate appointmentDate, LocalTime appointmentTime, String comment) {
        Appointment appointment = new Appointment(clientName, appointmentDate, appointmentTime, comment, doctor);

        // Link the appointment back to the doctor
        appointment.setDoctor(doctor);

        // Use a Set to maintain the appointment collection (even though there's only one)
        Set<Appointment> appointments = new HashSet<>();
        appointments.add(appointment);

        return appointments;
    }
}
