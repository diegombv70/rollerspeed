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

    // Mostrar formulario para crear un instructor
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "nuevo_instructor"; // Thymeleaf HTML
    }

    // Guardar instructor en base de datos
    @PostMapping("/guardar")
    public String guardarInstructor(@ModelAttribute Instructor instructor) {
        instructorRepository.save(instructor);
        return "redirect:/instructores/listar";
    }

    // Listar instructores
    @GetMapping("/listar")
    public String listarInstructores(Model model) {
        List<Instructor> instructores = instructorRepository.findAll();
        model.addAttribute("instructores", instructores);
        return "instructores"; // Thymeleaf HTML
    }
}
