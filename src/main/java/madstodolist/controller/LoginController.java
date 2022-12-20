package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;

    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginData", new LoginData());
        return "formLogin";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginData loginData, Model model, HttpSession session) {

        // Llamada al servicio para comprobar si el login es correcto
        UsuarioService.LoginStatus loginStatus = usuarioService.login(loginData.geteMail(), loginData.getPassword());

        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK) {
            Usuario usuario = usuarioService.findByEmail(loginData.geteMail());

            managerUserSession.logearUsuario(usuario.getId());

            if(usuario.getAdministrador()==true){
                return "redirect:/registrados";
            }
            else{
                return "redirect:/usuarios/" + usuario.getId() + "/tareas";
            }
        } else if (loginStatus == UsuarioService.LoginStatus.USER_NOT_FOUND) {
            model.addAttribute("error", "No existe usuario");
            return "formLogin";
        } else if (loginStatus == UsuarioService.LoginStatus.ERROR_PASSWORD) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "formLogin";
        } else if (loginStatus == UsuarioService.LoginStatus.ERROR_BLOQUEADO){
            model.addAttribute("error","El usuario esta bloqueado");
            return "formLogin";
        } else if (loginStatus == UsuarioService.LoginStatus.CUENTA_DESACTIVADA){
            model.addAttribute("error","Activala verificando el email");
            return "formLogin";
        }
        return "formLogin";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("admin",usuarioService.hayAdministrador());
        model.addAttribute("registroData", new RegistroData());
        return "formRegistro";
    }

   @PostMapping("/registro")
   public String registroSubmit(@Valid RegistroData registroData, BindingResult result, Model model, HttpServletRequest req) throws MessagingException, UnsupportedEncodingException {

        if (result.hasErrors()) {
            model.addAttribute("admin",usuarioService.hayAdministrador());
            return "formRegistro";
        }

        if (usuarioService.findByEmail(registroData.geteMail()) != null) {
            model.addAttribute("admin",usuarioService.hayAdministrador());
            model.addAttribute("registroData", registroData);
            model.addAttribute("error", "El usuario " + registroData.geteMail() + " ya existe");
            return "formRegistro";
        }
        Usuario usuario = new Usuario(registroData.geteMail());
        usuario.setPassword(registroData.getPassword());
        usuario.setFechaNacimiento(registroData.getFechaNacimiento());
        usuario.setNombre(registroData.getNombre());
        usuario.setAdministrador(registroData.getAdministrador());
        usuario.setAcceso(true);
        String randomCode = RandomString.make(64);
        usuario.setVerificationCode(randomCode);
        usuario.setEnabled(false);
        Usuario us = usuarioService.registrar(usuario);
        String url = usuarioService.getSiteURL(req);
        usuarioService.sendVerificationEmail(us,url);
        return "registro_check";
   }
    @GetMapping("/olvidar")
    public String olvidarForm(Model model) {
        model.addAttribute("resetData", new RessetData());
        return "formOlvidar";
    }
    @PostMapping("/olvidar")
    public String enviarCorreo(@ModelAttribute RessetData resetData, RedirectAttributes flash, Model model, HttpSession session, HttpServletRequest req) throws MessagingException, UnsupportedEncodingException {
        Usuario user = usuarioService.findByEmail(resetData.getEmail());

        if(user == null){
            flash.addFlashAttribute("error", "No existe el correo en el sistema");
            return "redirect:/olvidar";
        }
        else{
            String url = usuarioService.getSiteURL(req);
            usuarioService.sendRessetEmail(user,url);
            flash.addFlashAttribute("mensaje", "Correo enviado correctamente");
            return "redirect:/olvidar";
        }
    }
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (usuarioService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
    @GetMapping("/ressetPassword/{idUser}")
    public String resetPassword(@PathVariable("idUser") Long uid,Model model) {
        model.addAttribute("newPasswordData", new NewPasswordData());
        model.addAttribute("userId",uid);
        return "formNewPassword";
    }
    @PostMapping("/ressetPassword/{idUser}")
    public String resetPassword(@PathVariable("idUser") Long uid, RedirectAttributes flash, @ModelAttribute NewPasswordData newPasswordData, Model model, HttpSession session) {
        Usuario user = usuarioService.findById(uid);
        if(user == null){
            flash.addFlashAttribute("error","No se ha podido realizar el cambio de contraseña");
            return "redirect:/olvidar";
        }else{
            usuarioService.resetPassword(user.getId(),newPasswordData.getPassword());
            flash.addFlashAttribute("mensaje","Contraseña restablecida correctamente");
            return "redirect:/login";
        }
    }
   @GetMapping("/logout")
   public String logout(HttpSession session) {
        managerUserSession.logout();
        return "redirect:/login";
   }
}
