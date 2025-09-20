package com.rollerspeed.controller;

import com.rollerspeed.model.Clase;
import com.rollerspeed.model.Instructor;
import com.rollerspeed.repository.ClaseRepository;
import com.rollerspeed.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class ClaseController {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    // Mostrar formulario para registrar clase
    @GetMapping("/clases/nuevo")
    public String mostrarFormulario(Clase clase, Model model) {
        List<Instructor> instructores = instructorRepository.findAll();
        model.addAttribute("instructores", instructores);

        List<String> tipos = Arrays.asList(
                "Patinaje artístico",
                "Patinaje recreativo",
                "Patinaje en línea",
                "Hockey sobre patines",
                "Patinaje de velocidad",
                "Patinaje extremo / freestyle"
        );
        model.addAttribute("tipos", tipos);

        List<String> niveles = Arrays.asList("Principiante", "Intermedio", "Avanzado");
        model.addAttribute("niveles", niveles);

        return "nueva_clase"; // Thymeleaf HTML
    }

    // Guardar clase en base de datos
    @PostMapping("/clases/guardar")
    public String guardarClase(Clase clase) {
        // Cargar el instructor completo desde la BD usando el ID
        if (clase.getInstructor() != null && clase.getInstructor().getId() != null) {
            Instructor instructor = instructorRepository
                    .findById(clase.getInstructor().getId())
                    .orElse(null);
            clase.setInstructor(instructor);
        }
        claseRepository.save(clase);
        return "redirect:/clases/lista";
    }

    // Mostrar lista de clases
    @GetMapping("/clases/lista")
    public String listarClases(Model model) {
        List<Clase> clases = claseRepository.findAll();
        model.addAttribute("clases", clases);
        return "clase"; // Thymeleaf HTML
    }
}
