package com.rollerspeed.controller;

import com.rollerspeed.model.Clase;
import com.rollerspeed.model.Horario;
import com.rollerspeed.model.Registro;
import com.rollerspeed.model.Usuario;
import com.rollerspeed.repository.ClaseRepository;
import com.rollerspeed.repository.HorarioRepository;
import com.rollerspeed.repository.RegistroRepository;
import com.rollerspeed.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/registros")
public class RegistroController {

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    // Mostrar formulario de nuevo registro
    @GetMapping("/nuevo")
    public String mostrarFormulario(Registro registro, Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Clase> clases = claseRepository.findAll();

        model.addAttribute("registro", registro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("clases", clases);
        model.addAttribute("horarios", List.of()); // Se cargarán con JS al seleccionar clase
        return "nuevo_registro";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Registro registro = registroRepository.findById(id).orElse(null);
        if (registro == null) {
            return "redirect:/registros/listar";
        }

        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Clase> clases = claseRepository.findAll();
        List<Horario> horarios = registro.getClase() != null ? registro.getClase().getHorarios() : List.of();

        model.addAttribute("registro", registro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("clases", clases);
        model.addAttribute("horarios", horarios);
        return "editar_registro";
    }

    // Endpoint AJAX para cargar horarios de una clase
    @GetMapping("/horarios/{claseId}")
    @ResponseBody
    public List<Horario> obtenerHorarios(@PathVariable("claseId") Long claseId) {
        Clase clase = claseRepository.findById(claseId).orElse(null);
        if (clase != null) {
            return clase.getHorarios();
        }
        return List.of();
    }

    // Guardar inscripción nueva
    @PostMapping("/guardar")
    public String guardarRegistro(@RequestParam Long usuarioId,
                                  @RequestParam Long claseId,
                                  @RequestParam Long horarioId,
                                  Model model) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Clase clase = claseRepository.findById(claseId).orElse(null);
        Horario horario = horarioRepository.findById(horarioId).orElse(null);

        if (usuario == null || clase == null || horario == null) {
            model.addAttribute("error", "Usuario, clase o horario inválido");
            return "nuevo_registro";
        }

        Registro registro = new Registro();
        registro.setUsuario(usuario);
        registro.setClase(clase);
        registro.setHorario(horario);
        registroRepository.save(registro);

        return "redirect:/registros/listar";
    }

    // Actualizar registro existente
    @PostMapping("/actualizar/{id}")
    public String actualizarRegistro(@PathVariable("id") Long id,
                                     @RequestParam Long usuarioId,
                                     @RequestParam Long claseId,
                                     @RequestParam Long horarioId,
                                     Model model) {

        Registro registro = registroRepository.findById(id).orElse(null);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        Clase clase = claseRepository.findById(claseId).orElse(null);
        Horario horario = horarioRepository.findById(horarioId).orElse(null);

        if (registro == null || usuario == null || clase == null || horario == null) {
            model.addAttribute("error", "Datos inválidos para actualizar");
            return "editar_registro";
        }

        registro.setUsuario(usuario);
        registro.setClase(clase);
        registro.setHorario(horario);
        registroRepository.save(registro);

        return "redirect:/registros/listar";
    }

    // Listar inscripciones
    @GetMapping("/listar")
    public String listarRegistros(Model model) {
        List<Registro> registros = registroRepository.findAll();
        model.addAttribute("registros", registros);
        return "lista_registros";
    }

    // Eliminar inscripción
    @GetMapping("/eliminar/{id}")
    public String eliminarRegistro(@PathVariable("id") Long id) {
        registroRepository.deleteById(id);
        return "redirect:/registros/listar";
    }
}
