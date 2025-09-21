package com.rollerspeed.controller;

import com.rollerspeed.model.Instructor;
import com.rollerspeed.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/instructores")
public class InstructorController {

    @Autowired
    private InstructorRepository instructorRepository;

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "nuevo_instructor";
    }

    // Guarda
    @PostMapping("/guardar")
    public String guardarInstructor(@ModelAttribute Instructor instructor) {
        instructorRepository.save(instructor);
        return "redirect:/instructores/listar";
    }

    // Listarr
    @GetMapping("/listar")
    public String listarInstructores(Model model) {
        List<Instructor> instructores = instructorRepository.findAll();
        model.addAttribute("instructores", instructores);
        return "instructores";
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarInstructor(@PathVariable("id") Long id) {
        instructorRepository.deleteById(id);
        return "redirect:/instructores/listar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Instructor no encontrado con id: " + id));
        model.addAttribute("instructor", instructor);
        return "editar_instructor";
    }

    // Actualizar
    @PostMapping("/actualizar/{id}")
    public String actualizarInstructor(@PathVariable("id") Long id,
                                       @ModelAttribute Instructor instructor) {
        instructor.setId(id);
        instructorRepository.save(instructor);
        return "redirect:/instructores/listar";
    }
}
