package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.EquipoNotFoundException;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.controller.exception.UsuarioNoAdminException;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.model.Equipo;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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
    private void comprobarUsuarioAdmin(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoAdminException();
    }
    public boolean pertenezcoAlEquipo(Long idU,Long idE){
        return equipoService.searchUsuarioEquipo(idU,idE);
    }
    @GetMapping("/equipos")
    public String listadoEquipos(Model model, @Param("busca")String busca){
        //la primera linea para proteger el listado de equipos
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        List<Equipo> equipos = equipoService.buscador(busca);
        model.addAttribute("equipos",equipos);
        model.addAttribute("usuario", usuario);
        model.addAttribute("busca", busca);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(usuario.getId()));
        model.addAttribute("pertenecer",this);
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
        model.addAttribute("Idequipo",equipo.getId());
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(usuario.getId()));
        model.addAttribute("lider", equipo.getLider());
        return "miembrosEquipo";
    }
    @PostMapping("/equipos/{id}")
    @ResponseBody
    public String meterme(@PathVariable(value="id") Long idE, RedirectAttributes flash, HttpSession session){
        equipoService.addUsuarioEquipo(managerUserSession.usuarioLogeado(),idE);
        return "";
    }
    @DeleteMapping("/equipos/{id}")
    public String eliminarme(@PathVariable(value="id") Long idE, RedirectAttributes flash, HttpSession session){
        equipoService.deleteUsuarioEquipo(managerUserSession.usuarioLogeado(),idE);
        return "";
    }
    @DeleteMapping("/equipos/{id}/{idU}")
    public String eliminar(@PathVariable(value="id") Long idE,@PathVariable(value="idU") Long idU, RedirectAttributes flash, HttpSession session){
        equipoService.deleteUsuarioEquipo(idU,idE);
        return "";
    }
    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(@ModelAttribute EquipoData equipoData, Model model,
                                 HttpSession session) {
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        return "formNuevoEquipo";
    }
    @PostMapping("/equipos/nuevo")
    public String nuevoEquipo(@ModelAttribute EquipoData equipoData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {
        Long idUsuario = managerUserSession.usuarioLogeado();
        comprobarUsuarioLogeado(idUsuario);
        Usuario usuario = usuarioService.findById(idUsuario);
        equipoService.crearEquipo(equipoData.getNombre(), equipoData.getDescripcion(), usuario.getId());
        flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
        return "redirect:/equipos";
    }
    @GetMapping("/equipos/{id}/editar")
    public String formEditaEquipo(@PathVariable(value="id") Long idEquipo, @ModelAttribute EquipoData equipoData,
                                 Model model, HttpSession session) {

        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }
        comprobarUsuarioAdmin(usuarioService.devolverIDAdministrador());
        model.addAttribute("equipo", equipo);
        equipoData.setNombre(equipo.getNombre());
        equipoData.setDescripcion(equipo.getDescripcion());
        return "formEditarEquipo";
    }

    @PostMapping("/equipos/{id}/editar")
    public String grabaEquipoModificado(@PathVariable(value="id") Long idEquipo, @ModelAttribute EquipoData equipoData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }
        comprobarUsuarioAdmin(usuarioService.devolverIDAdministrador());

        equipoService.modificarEquipo(equipo, equipoData.getNombre(), equipoData.getDescripcion());
        flash.addFlashAttribute("mensaje", "Equipo modificado correctamente");
        return "redirect:/equipos";
    }
    @DeleteMapping("/equipos/{id}/eliminar")
    @ResponseBody
    public String eliminarEquipo(@PathVariable(value="id") Long idEquipo, RedirectAttributes flash, HttpSession session) {
        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }
        comprobarUsuarioAdmin(usuarioService.devolverIDAdministrador());

        equipoService.eliminarEquipo(equipo);
        return "";
    }
}
