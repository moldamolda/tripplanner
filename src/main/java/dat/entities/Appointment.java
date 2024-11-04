package dat.entities;

import dat.dtos.AppointmentDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "appointment")
@Data
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @EqualsAndHashCode.Exclude  // Exclude this to prevent circular dependency
    private Doctor doctor;

    // Optional: Constructor for easy initialization
    public Appointment(String clientName, LocalDate date, LocalTime time, String comment) {
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.comment = comment;
    }
    public Appointment(AppointmentDTO appointmentDTO) {
        this.id = appointmentDTO.getId();
        this.clientName = appointmentDTO.getClientName();
        this.date = appointmentDTO.getDate();
        this.time = appointmentDTO.getTime();
        this.comment = appointmentDTO.getComment();
        // Note: `doctor` field is not set here as it requires a separate reference to the `Doctor` entity.
    }

    public Appointment(String clientName, LocalDate date, LocalTime time, String comment, Doctor doctor) {
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.doctor = doctor; // Initialize doctor field
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment appointment = (Appointment) o;
        return Objects.equals(id, appointment.id) &&
                Objects.equals(date, appointment.date) &&
                Objects.equals(time, appointment.time) &&
                Objects.equals(clientName, appointment.clientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, time, clientName);
    }

}
