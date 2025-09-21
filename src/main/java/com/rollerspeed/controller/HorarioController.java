package com.rollerspeed.controller;

import com.rollerspeed.model.Horario;
import com.rollerspeed.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private HorarioRepository horarioRepository;

    // Listar horarios
    @GetMapping("/listar")
    public String listarHorarios(Model model) {
        List<Horario> horarios = horarioRepository.findAll();
        model.addAttribute("horarios", horarios);
        return "listar_horarios"; // Vista Thymeleaf
    }

    // Formulario nuevo horario
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("horario", new Horario()); // Objeto necesario para el formulario
        return "nuevo_horario"; // <-- Apunta al archivo en templates/ directamente
    }

    // Guardar horario
    @PostMapping("/guardar")
    public String guardarHorario(Horario horario) {
        horarioRepository.save(horario);
        return "redirect:/horarios/listar";
    }

    // Formulario editar horario
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Horario horario = horarioRepository.findById(id).orElse(null);
        if (horario == null) {
            return "redirect:/horarios/listar";
        }
        model.addAttribute("horario", horario);
        return "editar_horario";
    }

    // Actualizar horario
    @PostMapping("/actualizar/{id}")
    public String actualizarHorario(@PathVariable("id") Long id, Horario horario) {
        Horario existente = horarioRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setDescripcion(horario.getDescripcion());
            horarioRepository.save(existente);
        }
        return "redirect:/horarios/listar";
    }

    // Eliminar horario
    @GetMapping("/eliminar/{id}")
    public String eliminarHorario(@PathVariable("id") Long id) {
        horarioRepository.deleteById(id);
        return "redirect:/horarios/listar";
    }
}
