package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.entities.Appointment;
import dat.entities.Doctor;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentDTO {

    private Long id;

    private String clientName;

    private LocalDate date;

    private LocalTime time;

    private String comment;

    private Long doctorId;  // Store only the doctor's ID

    // Constructor for convenient initialization
    public AppointmentDTO(Long id, String clientName, LocalDate date, LocalTime time, String comment, Long doctorId) {
        this.id = id;
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.doctorId = doctorId;
    }
    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.clientName = appointment.getClientName();
        this.date = appointment.getDate();
        this.time = appointment.getTime();
        this.comment = appointment.getComment();

        // Optionally set doctor info if needed
        if (appointment.getDoctor() != null) {
            this.doctorId = appointment.getDoctor().getId();  // Assuming doctorId field exists in AppointmentDTO
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentDTO that = (AppointmentDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clientName, that.clientName) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientName, date, time);
    }


}
