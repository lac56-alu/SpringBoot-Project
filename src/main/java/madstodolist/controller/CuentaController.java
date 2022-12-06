package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class CuentaController {
    @Autowired
    ManagerUserSession managerUserSession;
    @Autowired
    UsuarioService usuarioService;

    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }

    @GetMapping("/cuenta")
    public String verCuenta(Model model, HttpSession session) {
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

    @GetMapping("/cuenta/modificar/{id}")
    public String formModificarTarea(@PathVariable(value="id") Long idUsuario,
                                 @ModelAttribute UsuarioData usuarioData, Model model,
                                 HttpSession session) {

        comprobarUsuarioLogeado(idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        model.addAttribute("usuario", usuario);
        return "formModificarCuenta";
    }

    @PostMapping("/cuenta/modificar/{id}")
    public String modificarCuenta(@PathVariable(value="id") Long idUsuario, @ModelAttribute UsuarioData usuarioData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
        comprobarUsuarioLogeado(idUsuario);

        Usuario us = new Usuario();
        us.setNombre(usuarioData.getNombre());
        us.setEmail(usuarioData.getEmail());
        us.setPassword(usuarioData.getPassword());
        us.setFechaNacimiento(usuarioData.getFechaNacimiento());

        usuarioService.modificarUsuario(idUsuario, us);

        Usuario usuario = usuarioService.findById(managerUserSession.usuarioLogeado());
        model.addAttribute("usuario", usuario);
        model.addAttribute("soyadmin",usuarioService.soyAdministrador(managerUserSession.usuarioLogeado()));
        flash.addFlashAttribute("mensaje", "Usuario modificado correctamente");
        return "cuenta";
    }

    @GetMapping("/cuenta/borrar/{id}")
    public String borrarCuenta(@PathVariable(value="id") Long idUsuario, Model model, RedirectAttributes flash, HttpSession session) {
        if(managerUserSession.usuarioLogeado()==null){
            throw new UsuarioNoLogeadoException();
        }

        comprobarUsuarioLogeado(idUsuario);
        usuarioService.borrarUsuario(idUsuario, managerUserSession.usuarioLogeado());

        model.addAttribute("error","Usuario borrado correctamente");
        model.addAttribute("loginData", new LoginData());
        flash.addFlashAttribute("mensaje", "Usuario borrado correctamente");
        return "redirect:/login";
    }
}