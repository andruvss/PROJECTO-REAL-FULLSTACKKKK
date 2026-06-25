package com.redsalud.patientservice.model;

import jakarta.persistence.*;
<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
=======
import jakarta.validation.constraints.*;
>>>>>>> 69f52dd17fdeeac74ce9e6b427a39624c6af3909

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUT es obligatorio")
    @Column(unique = true)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    private String email;

<<<<<<< HEAD
    // --- CONSTRUCTORES ---
    public Patient() {}

    // --- GETTERS Y SETTERS MANUALES ---
    // Esto es lo que Swagger necesita para mostrar los campos en el formulario

=======
    private String phone;

    // --- CONSTRUCTORES ---
    public Patient() {}

    public Patient(Long id, String rut, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.rut = rut;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    // --- GETTERS Y SETTERS (Cruciales para que PatientService funcione) ---
>>>>>>> 69f52dd17fdeeac74ce9e6b427a39624c6af3909
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
<<<<<<< HEAD
=======

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
>>>>>>> 69f52dd17fdeeac74ce9e6b427a39624c6af3909
}