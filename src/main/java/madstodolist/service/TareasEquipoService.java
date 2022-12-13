package madstodolist.service;

import madstodolist.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TareasEquipoService {
    Logger logger = LoggerFactory.getLogger(TareasEquipoService.class);

    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private TareasEquipoRepository tareasEquipoRepository;

    @Transactional
    public TareasEquipo nuevaTareaEquipo(Long idEquipo, String tituloTarea, String descripcionTarea, Date fechaTarea) {
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("No se ha encontrado ningún equipo con ese id");
        }
        TareasEquipo tareaEquipo = new TareasEquipo(equipo, tituloTarea, descripcionTarea, fechaTarea);
        tareasEquipoRepository.save(tareaEquipo);
        return tareaEquipo;
    }

    @Transactional(readOnly = true)
    public List<TareasEquipo> allTareasEquipo(Long idEquipo, String busca) {
        //logger.debug("Devolviendo todas las tareas del usuario " + idUsuario);
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("No se ha encontrado ningún equipo con ese id");
        }

        List<TareasEquipo> tareas = new ArrayList(equipo.getTareas());
        Collections.sort(tareas, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);

        if(busca!=null){
            return tareasEquipoRepository.busqueda(busca,idEquipo);
        }
        return tareas;
    }

    @Transactional(readOnly = true)
    public TareasEquipo findById(Long tareaId) {
        //logger.debug("Buscando tarea " + tareaId);
        return tareasEquipoRepository.findById(tareaId).orElse(null);
    }

    @Transactional
    public TareasEquipo modificarTareaEquipo(Long idTarea, String tituloTarea, String descripcionTarea, Date fechaTarea) {
        //logger.debug("Modificando tarea " + idTarea + " - " + nuevoTitulo);
        TareasEquipo tarea = tareasEquipoRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareasEquipoServiceException("No existe tarea con ese id");
        }
        tarea.setTitulo(tituloTarea);
        tarea.setDescripcion(descripcionTarea);
        tarea.setFecha(fechaTarea);
        tareasEquipoRepository.save(tarea);
        return tarea;
    }

    @Transactional
    public void borraTareaEquipo(Long idTarea) {
        //logger.debug("Borrando tarea " + idTarea);
        TareasEquipo tarea = tareasEquipoRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareasEquipoServiceException("No existe tarea con ese id");
        }
        tareasEquipoRepository.delete(tarea);
    }

    @Transactional
    public void guardarTareaEquipo(TareasEquipo tareasEquipo) {
        tareasEquipoRepository.save(tareasEquipo);
    }
}
