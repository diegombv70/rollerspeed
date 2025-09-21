package com.rollerspeed.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registros")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;  // Alumno inscrito

    @ManyToOne
    private Clase clase;      // Clase en la que se inscribe

    @ManyToOne
    private Horario horario;  // Horario seleccionado

    private LocalDate fechaRegistro = LocalDate.now();

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Clase getClase() { return clase; }
    public void setClase(Clase clase) { this.clase = clase; }

    public Horario getHorario() { return horario; }
    public void setHorario(Horario horario) { this.horario = horario; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
