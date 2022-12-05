package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import madstodolist.service.UsuarioServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CuentaController {
    @Autowired
    ManagerUserSession managerUserSession;
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/cuenta")
    public String about(Model model) {
        if(managerUserSession.usuarioLogeado()==null){
            throw new UsuarioNoLogeadoException();
        }
        else{
            Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
            model.addAttribute("usuario", usuario);
            model.addAttribute("soyadmin",usuarioService.soyAdministrador(managerUserSession.usuarioLogeado()));
            return "cuenta";
        }
    }
}