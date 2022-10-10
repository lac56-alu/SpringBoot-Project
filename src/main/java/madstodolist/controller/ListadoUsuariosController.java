package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.controller.exception.UsuarioNoAdminException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ListadoUsuariosController {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ManagerUserSession managerUserSession;

    private void comprobarUsuarioAdmin(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoAdminException();
    }
    private Long getIdAdministrador(){
        return usuarioService.devolverIDAdministrador();
    }
    @GetMapping("/registrados")
    public String listadoUsuarios(Model model){
        Long idAdmin = getIdAdministrador();
        comprobarUsuarioAdmin(idAdmin);
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios",usuarios);
        model.addAttribute("usuario", usuario);
        return "listaUsuarios";
    }
    @PutMapping("/registrados/{id}")
    @ResponseBody
    // La anotación @ResponseBody sirve para que la cadena devuelta sea la resupuesta
    // de la petición HTTP, en lugar de una plantilla thymeleaf
    public String bloquearUsuario(@PathVariable(value="id") Long idUsuario, RedirectAttributes flash, HttpSession session) {
        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        comprobarUsuarioAdmin(usuarioService.devolverIDAdministrador());
        usuarioService.bloquearUsuario(idUsuario);
        return "";
    }
    @PostMapping("/registrados/{id}")
    @ResponseBody
    // La anotación @ResponseBody sirve para que la cadena devuelta sea la resupuesta
    // de la petición HTTP, en lugar de una plantilla thymeleaf
    public String habilitarUsuario(@PathVariable(value="id") Long idUsuario, RedirectAttributes flash, HttpSession session) {
        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        comprobarUsuarioAdmin(usuarioService.devolverIDAdministrador());
        usuarioService.habiliarUsuario(idUsuario);
        return "";
    }

}
