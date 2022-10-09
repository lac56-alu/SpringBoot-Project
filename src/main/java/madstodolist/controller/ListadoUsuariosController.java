package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoAdminException;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
}
