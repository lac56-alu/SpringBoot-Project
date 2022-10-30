package madstodolist.service;

import madstodolist.controller.exception.EquipoNoNameException;
import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EquipoService {
    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    @Autowired
    EquipoRepository equipoRepository;
    @Autowired
    UsuarioService usuarioService;

    @Transactional
    public Equipo crearEquipo(String nombre) {
        if(nombre==""){
            throw new EquipoNoNameException();
        }
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);
        return equipo;
    }
    @Transactional(readOnly = true)
    public Equipo recuperarEquipo(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        return equipo;
    }
    @Transactional(readOnly = false)
    public List<Equipo> findAllOrderedByName(){
        List<Equipo>equipos = equipoRepository.findAll();
        Collections.sort(equipos,
                (Equipo e1,Equipo e2)->e1.getNombre().compareTo(e2.getNombre())
        );
        return equipos;
    }
    @Transactional(readOnly = true)
    public Equipo findById(Long id){
        return equipoRepository.findById(id).orElse(null);
    }
    @Transactional
    public void addUsuarioEquipo(Long idU,Long idE){
        Equipo equipo = this.recuperarEquipo(idE);
        Usuario usuario = usuarioService.findById(idU);
        equipo.addUsuario(usuario);
    }
    @Transactional(readOnly = true)
    public boolean searchUsuarioEquipo(Long idU,Long idE){
        boolean encontrado=false;
        List<Usuario>users = usuariosEquipo(idE);
        for(int i=0;i<users.size()&&encontrado==false;i++){
            if(users.get(i).getId()==idU){
                encontrado = true;
            }
        }
        return encontrado;
    }
    @Transactional(readOnly = true)
    public List<Usuario>usuariosEquipo(Long idE){
        Equipo eq = this.recuperarEquipo(idE);
        List<Usuario> users = new ArrayList<>(eq.getUsuarios());
        return users;
    }
    @Transactional
    public void deleteUsuarioEquipo(Long idU,Long idE){
        Equipo equipo = this.recuperarEquipo(idE);
        Usuario usuario = usuarioService.findById(idU);
        equipo.deleteUsuario(usuario);
    }
    @Transactional
    public void eliminarEquipo(Equipo equipo) {
        if(equipo == null) {
            throw new EquipoServiceException("No existe equipo con id " + equipo.getId());
        }
        equipoRepository.delete(equipo);
    }
}