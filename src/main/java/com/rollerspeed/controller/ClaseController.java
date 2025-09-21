package com.rollerspeed.controller;

import com.rollerspeed.model.Clase;
import com.rollerspeed.model.Horario;
import com.rollerspeed.model.Instructor;
import com.rollerspeed.repository.ClaseRepository;
import com.rollerspeed.repository.HorarioRepository;
import com.rollerspeed.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/clases")
public class ClaseController {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @GetMapping("/nuevo")
    public String mostrarFormulario(Clase clase, Model model) {
        cargarListas(model);
        return "nueva_clase";
    }

    // Guardar
    @PostMapping("/guardar")
    public String guardarClase(Clase clase, @RequestParam(required = false) List<Long> horarioIds) {
        // Asociar instructor
        if (clase.getInstructor() != null && clase.getInstructor().getId() != null) {
            Instructor instructor = instructorRepository.findById(clase.getInstructor().getId()).orElse(null);
            clase.setInstructor(instructor);
        }

        if (horarioIds != null && !horarioIds.isEmpty()) {
            List<Horario> horariosSeleccionados = horarioRepository.findAllById(horarioIds);
            clase.setHorarios(horariosSeleccionados);

            for (Horario h : horariosSeleccionados) {
                if (h.getClases() == null) {
                    h.setClases(new ArrayList<>());
                }
                h.getClases().add(clase);
            }
        } else {
            clase.setHorarios(new ArrayList<>());
        }

        claseRepository.save(clase);
        return "redirect:/clases/listar";
    }

    // Listar
    @GetMapping("/listar")
    public String listarClases(Model model) {
        List<Clase> clases = claseRepository.findAll();
        List<Horario> horarios = horarioRepository.findAll();
        model.addAttribute("clases", clases);
        model.addAttribute("horarios", horarios); 
        return "clase";
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarClase(@PathVariable("id") Long id) {
        Clase clase = claseRepository.findById(id).orElse(null);
        if (clase != null) {
            
            if (clase.getHorarios() != null) {
                for (Horario h : clase.getHorarios()) {
                    h.getClases().remove(clase);
                }
            }
            claseRepository.delete(clase);
        }
        return "redirect:/clases/listar";
    }

    // Editar
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Clase clase = claseRepository.findById(id).orElse(null);
        if (clase == null) {
            return "redirect:/clases/listar";
        }
        model.addAttribute("clase", clase);
        cargarListas(model);
        return "editar_clase";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarClase(@PathVariable("id") Long id, Clase clase, 
                                  @RequestParam(required = false) List<Long> horarioIds) {
        Clase existente = claseRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNombre(clase.getNombre());
            existente.setNivel(clase.getNivel());

            if (clase.getInstructor() != null && clase.getInstructor().getId() != null) {
                Instructor instructor = instructorRepository.findById(clase.getInstructor().getId()).orElse(null);
                existente.setInstructor(instructor);
            } else {
                existente.setInstructor(null);
            }
            if (existente.getHorarios() != null) {
                for (Horario h : existente.getHorarios()) {
                    h.getClases().remove(existente);
                }
            }

            if (horarioIds != null && !horarioIds.isEmpty()) {
                List<Horario> horariosSeleccionados = horarioRepository.findAllById(horarioIds);
                existente.setHorarios(horariosSeleccionados);

                for (Horario h : horariosSeleccionados) {
                    if (h.getClases() == null) {
                        h.setClases(new ArrayList<>());
                    }
                    h.getClases().add(existente);
                }
            } else {
                existente.setHorarios(new ArrayList<>());
            }

            claseRepository.save(existente);
        }
        return "redirect:/clases/listar";
    }

    private void cargarListas(Model model) {
        model.addAttribute("instructores", instructorRepository.findAll());
        model.addAttribute("horarios", horarioRepository.findAll());
        model.addAttribute("tipos", Arrays.asList(
                "Patinaje artístico",
                "Patinaje recreativo",
                "Patinaje en línea",
                "Hockey sobre patines",
                "Patinaje de velocidad",
                "Patinaje extremo / freestyle"
        ));
        model.addAttribute("niveles", Arrays.asList("Principiante", "Intermedio", "Avanzado"));
    }
}
