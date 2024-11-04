package dat.entities;

import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity

@Table(name = "doctor")
public class Doctor {

    //husk @Tostrin.Exclude hvis du ikke vil have noget med i toString metoden
    //husk @JsonBackReference og @JsonManagedReference(one to many-siden) hvis du vil undgå uendelige loops

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id", nullable = false, unique = true)
    private Long id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Setter
    @Column(name = "year_of_graduation", nullable = false)
    private int yearOfGraduation;

    @Setter
    @Column(name = "name_of_clinic", nullable = false)
    private String nameOfClinic;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "speciality", nullable = false)
    private Speciality speciality;

    @Column(nullable = false, updatable = false)
    @ToString.Exclude
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude  // Exclude this to prevent circular dependency
    private Set<Appointment> appointments = new HashSet<>();

    // Optional: Constructor for easier initialization
    public Doctor(String name, LocalDate dateOfBirth, int yearOfGraduation, String nameOfClinic, Speciality speciality) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.yearOfGraduation = yearOfGraduation;
        this.nameOfClinic = nameOfClinic;
        this.speciality = speciality;
    }
    public Doctor(DoctorDTO doctorDTO) {
        this.name = doctorDTO.getName();
        this.dateOfBirth = doctorDTO.getDateOfBirth();
        this.yearOfGraduation = doctorDTO.getYearOfGraduation();
        this.nameOfClinic = doctorDTO.getNameOfClinic();
        this.speciality = doctorDTO.getSpeciality();
        // Note: `appointments` are not included here as they may require a separate handling in case of bidirectional association.
    }


    // Helper method to add an appointment
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setDoctor(this);  // Maintain bidirectional relationship
    }

    // Methods to handle automatic timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id) &&
                Objects.equals(name, doctor.name) &&
                Objects.equals(dateOfBirth, doctor.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth);
    }



}
