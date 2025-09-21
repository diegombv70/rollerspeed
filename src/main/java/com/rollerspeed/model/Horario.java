package com.rollerspeed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "horarios")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion; // Ej: "Lunes 10:00 - 12:00"

    @ManyToMany(mappedBy = "horarios")
    @JsonIgnoreProperties("horarios") // Evita loop infinito
    private List<Clase> clases = new ArrayList<>();

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Clase> getClases() { return clases; }
    public void setClases(List<Clase> clases) { this.clases = clases; }

    @Override
    public String toString() {
        return descripcion;
    }
}
