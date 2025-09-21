package com.rollerspeed.controller;

import com.rollerspeed.model.Asistencia;
import com.rollerspeed.model.Clase;
import com.rollerspeed.model.Usuario;
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
    public String mostrarFormulario(Asistencia asistencia, Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Clase> clases = claseRepository.findAll();
        model.addAttribute("asistencia", asistencia);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("clases", clases);
        return "nueva_asistencia";
    }

    // Guardar
    @PostMapping("/guardar")
    public String guardarAsistencia(@RequestParam Long usuarioId,
                                    @RequestParam Long claseId,
                                    @RequestParam(required = false) boolean presente,
                                    Model model) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Clase clase = claseRepository.findById(claseId).orElse(null);

        if (usuario == null || clase == null) {
            model.addAttribute("error", "Usuario o clase inválidos");
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("clases", claseRepository.findAll());
            return "nueva_asistencia";
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setAlumno(usuario);
        asistencia.setClase(clase);
        asistencia.setPresente(presente);
        asistenciaRepository.save(asistencia);

        return "redirect:/asistencias/listar";
    }

    // Listar
    @GetMapping("/listar")
    public String listarAsistencias(Model model) {
        List<Asistencia> asistencias = asistenciaRepository.findAll();
        model.addAttribute("asistencias", asistencias);
        return "lista_asistencias";
    }

    @GetMapping("/editar/{id}")
    public String editarAsistencia(@PathVariable Long id, Model model) {
        Asistencia asistencia = asistenciaRepository.findById(id).orElse(null);
        if (asistencia == null) {
            return "redirect:/asistencias/listar";
        }
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Clase> clases = claseRepository.findAll();
        model.addAttribute("asistencia", asistencia);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("clases", clases);
        return "editar_asistencia";
    }

    // Actualizar
    @PostMapping("/actualizar/{id}")
    public String actualizarAsistencia(@PathVariable Long id,
                                    @RequestParam(required = false) boolean presente) {
        Asistencia asistencia = asistenciaRepository.findById(id).orElse(null);
        if (asistencia != null) {
            asistencia.setPresente(presente);
            asistenciaRepository.save(asistencia);
        }
        return "redirect:/asistencias/listar";
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarAsistencia(@PathVariable Long id) {
        asistenciaRepository.deleteById(id);
        return "redirect:/asistencias/listar";
    }
}
