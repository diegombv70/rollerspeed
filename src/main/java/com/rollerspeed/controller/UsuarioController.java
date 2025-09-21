package com.rollerspeed.controller;

import com.rollerspeed.model.Clase;
import com.rollerspeed.model.Usuario;
import com.rollerspeed.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    
    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "nuevo_usuario";
    }

    
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, 
                                 @RequestParam("rol") String rol,
                                 @RequestParam("clase") String clase) {
        usuario.setRol(rol);
        usuario.setClase(clase);
        usuarioRepository.save(usuario);
        return "redirect:/usuarios/listar";
    }

    
    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios/listar";
    }

    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
        model.addAttribute("usuario", usuario);
        return "editar_usuario";
    }

    
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable("id") Long id,
                                    @ModelAttribute Usuario usuario,
                                    @RequestParam("rol") String rol,
                                    @RequestParam("clase") String clase) {
        usuario.setId(id);
        usuario.setRol(rol);
        usuario.setClase(clase);
        usuarioRepository.save(usuario);
        return "redirect:/usuarios/listar";
    }

}
