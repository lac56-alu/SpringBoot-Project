package madstodolist.service;

import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class UsuarioService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD, ERROR_BLOQUEADO}

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public LoginStatus login(String eMail, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(eMail);
        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        } else if (!usuario.get().getPassword().equals(password)) {
            return LoginStatus.ERROR_PASSWORD;
        } else if(usuario.get().getAcceso()==false){
            return LoginStatus.ERROR_BLOQUEADO;
        }else {
            return LoginStatus.LOGIN_OK;
        }
    }

    // Se añade un usuario en la aplicación.
    // El email y password del usuario deben ser distinto de null
    // El email no debe estar registrado en la base de datos
    @Transactional
    public Usuario registrar(Usuario usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioBD.isPresent())
            throw new UsuarioServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        else if (usuario.getEmail() == null)
            throw new UsuarioServiceException("El usuario no tiene email");
        else if (usuario.getPassword() == null)
            throw new UsuarioServiceException("El usuario no tiene password");
        else return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).orElse(null);
    }
    @Transactional(readOnly = true)
    public List<Usuario>findAll() {
        return (List<Usuario>) usuarioRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<Usuario>findAllMenosLogueado(){
        List<Usuario>usuarios = findAll();
        List<Usuario>users = new ArrayList<>();
        for(int i=0;i<usuarios.size();i++){
            if(usuarios.get(i).getAdministrador()==false){
                users.add(usuarios.get(i));
            }
        }
        return users;
    }
    @Transactional(readOnly = true)
    public boolean hayAdministrador(){
        List<Usuario> users = findAll();
        boolean hayadmin=false;
        for(int i=0;i<users.size() && hayadmin == false;i++){
            if(users.get(i).getAdministrador()==true){
                hayadmin = true;
            }
        }
        return hayadmin;
    }
    @Transactional(readOnly = true)
    public Long devolverIDAdministrador(){
        Long id = -1L;
        List<Usuario>users = findAll();
        for(int i=0;i< users.size() && id==-1L;i++){
            if(users.get(i).getAdministrador()==true){
                id = users.get(i).getId();
            }
        }
        return id;
    }
    @Transactional(readOnly = true)
    public boolean soyAdministrador(Long id) {
        Usuario user = findById(id);
        return user.getAdministrador();
    }
    @Transactional(readOnly = false)
    public void bloquearUsuario(Long id){
        Usuario us = findById(id);
        us.setAcceso(false);
    }
    @Transactional(readOnly = false)
    public void habiliarUsuario(Long id){
        Usuario us = findById(id);
        us.setAcceso(true);
    }

    @Transactional(readOnly = false)
    public void modificarUsuario(Long idUsuario, Usuario modificar){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if(usuario != null){
            usuario.setEmail(modificar.getEmail());
            usuario.setPassword(modificar.getPassword());
            usuario.setNombre(modificar.getNombre());
            usuario.setFechaNacimiento(modificar.getFechaNacimiento());
            usuarioRepository.save(usuario);
        }
        else{
            throw new UsuarioServiceException("Usuario erroneo, no se puede modificar...");
        }

    }

    @Transactional(readOnly = false)
    public void borrarUsuario(Long idUsuarioRegistrado, Long idUsuarioBorrar){
        if(idUsuarioBorrar != idUsuarioRegistrado){
            throw new UsuarioServiceException("No se puede borrar un usuario que no sea propio...");
        }
        usuarioRepository.deleteById(idUsuarioRegistrado);
    }
}
