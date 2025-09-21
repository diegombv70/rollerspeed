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

    @GetMapping("/nuevo")
    public String mostrarFormulario(Registro registro, Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Clase> clases = claseRepository.findAll();

        model.addAttribute("registro", registro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("clases", clases);
        model.addAttribute("horarios", List.of());
        return "nuevo_registro";
    }

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

    @GetMapping("/horarios/{claseId}")
    @ResponseBody
    public List<Horario> obtenerHorarios(@PathVariable("claseId") Long claseId) {
        Clase clase = claseRepository.findById(claseId).orElse(null);
        if (clase != null) {
            return clase.getHorarios();
        }
        return List.of();
    }

    @PostMapping("/guardar")
    public String guardarRegistro(@ModelAttribute("registro") Registro registro, Model model) {
        if (registro.getUsuario() == null || registro.getClase() == null || registro.getHorario() == null) {
            model.addAttribute("error", "Usuario, clase o horario inválido");
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("clases", claseRepository.findAll());
            model.addAttribute("horarios", registro.getClase() != null ? registro.getClase().getHorarios() : List.of());
            return "nuevo_registro";
        }

        registroRepository.save(registro);
        return "redirect:/registros/listar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarRegistro(@PathVariable("id") Long id,
                                     @ModelAttribute("registro") Registro registroForm,
                                     Model model) {

        Registro registro = registroRepository.findById(id).orElse(null);
        if (registro == null || registroForm.getUsuario() == null ||
            registroForm.getClase() == null || registroForm.getHorario() == null) {

            model.addAttribute("error", "Datos inválidos para actualizar");
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("clases", claseRepository.findAll());
            model.addAttribute("horarios", registroForm.getClase() != null ? registroForm.getClase().getHorarios() : List.of());
            return "editar_registro";
        }

        registro.setUsuario(registroForm.getUsuario());
        registro.setClase(registroForm.getClase());
        registro.setHorario(registroForm.getHorario());
        registroRepository.save(registro);

        return "redirect:/registros/listar";
    }

    @GetMapping("/listar")
    public String listarRegistros(Model model) {
        List<Registro> registros = registroRepository.findAll();
        model.addAttribute("registros", registros);
        return "lista_registros";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRegistro(@PathVariable("id") Long id) {
        registroRepository.deleteById(id);
        return "redirect:/registros/listar";
    }
}
