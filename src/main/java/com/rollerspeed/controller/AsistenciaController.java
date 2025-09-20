package com.rollerspeed.controller;

import com.rollerspeed.model.Asistencia;
import com.rollerspeed.repository.AsistenciaRepository;
import com.rollerspeed.repository.ClaseRepository;
import com.rollerspeed.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClaseRepository claseRepository;

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("asistencia", new Asistencia());
        model.addAttribute("alumnos", usuarioRepository.findAll());
        model.addAttribute("clases", claseRepository.findAll());
        return "nueva_asistencia";
    }

    @PostMapping("/guardar")
    public String guardarAsistencia(@ModelAttribute Asistencia asistencia) {
        asistenciaRepository.save(asistencia);
        return "redirect:/asistencias/listar";
    }

    @GetMapping("/listar")
    public String listarAsistencias(Model model) {
        List<Asistencia> asistencias = asistenciaRepository.findAll();
        model.addAttribute("asistencias", asistencias);
        return "asistencias";
    }
}
