package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.controller.exception.UsuarioNoAdminException;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.model.*;
import madstodolist.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TareasEquipoController {
    @Autowired
    EquipoService equipoService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    TareasEquipoService tareasEquipoService;
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

    @GetMapping("/equipo/{id}/listaTareas")
    public String listaTareasEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session){
        //la primera linea para proteger el equipo
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);

        Equipo equipo = equipoService.findById(idEquipo);
        Set<TareasEquipo> tareas =equipoService.findById(equipo.getId()).getTareas();

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("tareas", tareas);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(usuario.getId()));
        return "listaTareasEquipo";
    }

    @GetMapping("/equipo/{idEquipo}/modificarTarea/{idTarea}")
    public String listaTareasEquipo(@PathVariable(value="idEquipo") Long idEquipo, @PathVariable(value="idTarea") Long idTarea,
                                    Model model, HttpSession session, TareasEquipoData tareasEquipoData){
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);

        Equipo equipo = equipoService.findById(idEquipo);
        if(equipo == null){
            throw new EquipoServiceException("Equipo con ese id no encontrado");
        }

        TareasEquipo tarea = tareasEquipoService.findById(idTarea);
        if(tarea == null){
            throw new TareasEquipoServiceException("Tarea con ese id no encontrada");
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("tarea", tarea);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(usuario.getId()));
        return "formModificarTareaEquipo";
    }

    @PostMapping("/equipo/{idEquipo}/modificarTarea/{idTarea}")
    public String modificarTareaEquipo(@PathVariable(value="idEquipo") Long idEquipo, @PathVariable(value="idTarea") Long idTarea,
                                       @ModelAttribute TareasEquipoData tareasEquipoData,
                                  Model model, RedirectAttributes flash, HttpSession session) {
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);
        Equipo equipo = equipoService.findById(idEquipo);
        if(equipo == null){
            throw new EquipoServiceException("Equipo con ese id no encontrado");
        }
        TareasEquipo tarea = tareasEquipoService.findById(idTarea);
        if(tarea == null){
            throw new TareasEquipoServiceException("Tarea con ese id no encontrada");
        }

        tarea.setTitulo(tareasEquipoData.getTitulo());
        tarea.setDescripcion(tareasEquipoData.getDescripcion());
        tarea.setFecha(tareasEquipoData.getFecha());
        tareasEquipoService.guardarTareaEquipo(tarea);

        Set<TareasEquipo> tareas =equipoService.findById(equipo.getId()).getTareas();

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("tareas", tareas);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(managerUserSession.usuarioLogeado()));
        flash.addFlashAttribute("mensaje", "Tarea modificada correctamente");
        return "listaTareasEquipo";
    }

    @DeleteMapping("/equipo/{idEquipo}/borrarTarea/{idTarea}")
    @ResponseBody
    public String borrarTarea(@PathVariable(value="idEquipo") Long idEquipo, @PathVariable(value="idTarea") Long idTarea,
                              Model model, RedirectAttributes flash, HttpSession session) {
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);
        Equipo equipo = equipoService.findById(idEquipo);
        if(equipo == null){
            throw new EquipoServiceException("Equipo con ese id no encontrado");
        }
        TareasEquipo tarea = tareasEquipoService.findById(idTarea);
        if(tarea == null){
            throw new TareasEquipoServiceException("Tarea con ese id no encontrada");
        }
        tareasEquipoService.borraTareaEquipo(idTarea);
        Set<TareasEquipo> tareas =equipoService.findById(equipo.getId()).getTareas();

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("tareas", tareas);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(usuario.getId()));
        return "listaTareasEquipo";
    }

    @GetMapping("/equipo/{idEquipo}/crearTarea")
    public String crearTareasEquipo(@PathVariable(value="idEquipo") Long idEquipo,
                                    Model model, HttpSession session, TareasEquipoData tareasEquipoData){
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);

        Equipo equipo = equipoService.findById(idEquipo);
        if(equipo == null){
            throw new EquipoServiceException("Equipo con ese id no encontrado");
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(usuario.getId()));
        return "formCrearTareaEquipo";
    }

    @PostMapping("/equipo/{idEquipo}/crearTarea")
    public String crearPostTareaEquipo(@PathVariable(value="idEquipo") Long idEquipo,
                                       @ModelAttribute TareasEquipoData tareasEquipoData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);
        Equipo equipo = equipoService.findById(idEquipo);
        if(equipo == null){
            throw new EquipoServiceException("Equipo con ese id no encontrado");
        }

        tareasEquipoService.nuevaTareaEquipo(equipo.getId(),tareasEquipoData.getTitulo(),tareasEquipoData.getDescripcion(),tareasEquipoData.getFecha());
        Set<TareasEquipo> tareas = equipoService.findById(equipo.getId()).getTareas();

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("tareas", tareas);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(managerUserSession.usuarioLogeado()));
        flash.addFlashAttribute("mensaje", "Tarea creada correctamente");
        return "listaTareasEquipo";
    }


    @GetMapping("/equipo/{idEquipo}/modificarEstado/{idTarea}")
    public String mostrarTareaEstado(@PathVariable(value="idEquipo") Long idEquipo, @PathVariable(value="idTarea") Long idTarea,
                                    Model model, HttpSession session, EstadoTareaEquipoData estadoTareaEquipoData){
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);

        Equipo equipo = equipoService.findById(idEquipo);
        if(equipo == null){
            throw new EquipoServiceException("Equipo con ese id no encontrado");
        }

        TareasEquipo tarea = tareasEquipoService.findById(idTarea);
        if(tarea == null){
            throw new TareasEquipoServiceException("Tarea con ese id no encontrada");
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("tarea", tarea);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(usuario.getId()));
        return "formEstadoTareaEquipo";
    }

    @PostMapping("/equipo/{idEquipo}/modificarEstado/{idTarea}")
    public String modificarEstadoTareaEquipo(@PathVariable(value="idEquipo") Long idEquipo, @PathVariable(value="idTarea") Long idTarea,
                                       @ModelAttribute EstadoTareaEquipoData estadoTareaEquipoData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
        comprobarUsuarioLogeado(managerUserSession.usuarioLogeado());
        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);
        Equipo equipo = equipoService.findById(idEquipo);
        if(equipo == null){
            throw new EquipoServiceException("Equipo con ese id no encontrado");
        }
        TareasEquipo tarea = tareasEquipoService.findById(idTarea);
        if(tarea == null){
            throw new TareasEquipoServiceException("Tarea con ese id no encontrada");
        }

        tareasEquipoService.modificarEstadoTareaEquipo(tarea.getId(), estadoTareaEquipoData.getEstado());

        Set<TareasEquipo> tareas =equipoService.findById(equipo.getId()).getTareas();

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("tareas", tareas);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(managerUserSession.usuarioLogeado()));
        flash.addFlashAttribute("mensaje", "Tarea modificada correctamente");
        return "listaTareasEquipo";
    }
}
