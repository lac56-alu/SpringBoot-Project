package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class EquiposController {
    @Autowired
    EquipoService equipoService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ManagerUserSession managerUserSession;
    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }
    @GetMapping("/equipos")
    public String listadoEquipos(Model model){
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        List<Equipo> equipos = equipoService.findAllOrderedByName();
        model.addAttribute("equipos",equipos);
        model.addAttribute("usuario", usuario);
        return "listaEquipos";
    }
}
