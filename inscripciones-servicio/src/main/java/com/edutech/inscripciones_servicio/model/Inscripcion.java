package com.edutech.inscripciones_servicio.model; // PAQUETE CORREGIDO

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.LocalDate;

@Entity // Indica que esta clase es una entidad JPA
public class Inscripcion {

    @Id // Marca el campo 'id' como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID
    private Long id;
    private Long userId; // Nombre del campo corregido a userId
    private Long cursoId; // Nombre del campo corregido a cursoId
    private LocalDate fechaInscripcion;

    @Enumerated(EnumType.STRING) // Almacena el enum como String en la base de datos
    private EstadoInscripcion status; // Nombre del campo corregido a status

    // Constructor vacío requerido por JPA
    public Inscripcion() {
    }

    // Constructor con todos los campos
    public Inscripcion(Long id, Long userId, Long cursoId, LocalDate fechaInscripcion, EstadoInscripcion status) {
        this.id = id;
        this.userId = userId;
        this.cursoId = cursoId;
        this.fechaInscripcion = fechaInscripcion;
        this.status = status;
    }

    // Getters y Setters para todos los campos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() { // Getter corregido
        return userId;
    }

    public void setUserId(Long userId) { // Setter corregido
        this.userId = userId;
    }

    public Long getCursoId() { // Getter corregido
        return cursoId;
    }

    public void setCursoId(Long cursoId) { // Setter corregido
        this.cursoId = cursoId;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public EstadoInscripcion getStatus() { // Getter corregido
        return status;
    }

    public void setStatus(EstadoInscripcion status) { // Setter corregido
        this.status = status;
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
               "id=" + id +
               ", userId=" + userId +
               ", cursoId=" + cursoId +
               ", fechaInscripcion=" + fechaInscripcion +
               ", status=" + status +
               '}';
    }
}
