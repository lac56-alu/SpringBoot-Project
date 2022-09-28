package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    ManagerUserSession managerUserSession;
    @Autowired
    UsuarioService usuarioService;
    @GetMapping("/about")
    public String about(Model model) {
        if(managerUserSession.usuarioLogeado()==null){
            return "aboutNoLogueado";
        }
        else{
            Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
            model.addAttribute("usuario", usuario);
            return "about";
        }
    }
    @GetMapping("/tareas")
    public String irTarea(Model model) {
        return "redirect:/usuarios/" + managerUserSession.usuarioLogeado() + "/tareas";
    }

}