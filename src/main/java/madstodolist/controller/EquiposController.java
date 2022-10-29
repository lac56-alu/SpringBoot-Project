package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EquiposController {
    @Autowired
    EquipoService equipoService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ManagerUserSession managerUserSession;
    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if(idUsuarioLogeado == null)
            throw new UsuarioNoLogeadoException();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }
    @GetMapping("/equipos")
    public String listadoEquipos(Model model){
        //la primera linea para proteger el listado de equipos
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        List<Equipo> equipos = equipoService.findAllOrderedByName();
        model.addAttribute("equipos",equipos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(usuario.getId()));
        return "listaEquipos";
    }
    @GetMapping("/equipos/{id}")
    public String miembrosEquipo(@PathVariable(value="id") Long idEquipo, Model model){
        //la primera linea para proteger el equipo
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);
        Equipo equipo = equipoService.findById(idEquipo);
        List<Usuario> users = equipoService.usuariosEquipo(equipo.getId());
        model.addAttribute("users",users);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(usuario.getId()));
        return "miembrosEquipo";
    }
    @PostMapping("/equipos/{id}")
    @ResponseBody
    public String meterme(Model model){

        return "";
    }
    @DeleteMapping("/equipos/{id}")
    public String eliminarme(Model model){
        
        return "";
    }
}
