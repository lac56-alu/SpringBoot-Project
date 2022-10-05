package madstodolist.controller;

import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class DescripcionUsuarioController {
    @Autowired
    UsuarioService usuarioService;
    @GetMapping("/registrados/{id}")
    public String descripcionUsuario(@PathVariable(value="id") Long idUsuario,Model model){
        Usuario usuario = usuarioService.findById(idUsuario);
        model.addAttribute("usuario",usuario);
        return "descripcionUsuario";
    }
}
