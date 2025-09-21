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

    // Listar
    @GetMapping("/listar")
    public String listarHorarios(Model model) {
        List<Horario> horarios = horarioRepository.findAll();
        model.addAttribute("horarios", horarios);
        return "listar_horarios"; 
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("horario", new Horario());
        return "nuevo_horario";
    }

    // Guardar
    @PostMapping("/guardar")
    public String guardarHorario(Horario horario) {
        horarioRepository.save(horario);
        return "redirect:/horarios/listar";
    }

    //editar
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Horario horario = horarioRepository.findById(id).orElse(null);
        if (horario == null) {
            return "redirect:/horarios/listar";
        }
        model.addAttribute("horario", horario);
        return "editar_horario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarHorario(@PathVariable("id") Long id, Horario horario) {
        Horario existente = horarioRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setDescripcion(horario.getDescripcion());
            horarioRepository.save(existente);
        }
        return "redirect:/horarios/listar";
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarHorario(@PathVariable("id") Long id) {
        horarioRepository.deleteById(id);
        return "redirect:/horarios/listar";
    }
}
