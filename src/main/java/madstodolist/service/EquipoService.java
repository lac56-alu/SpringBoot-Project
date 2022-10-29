package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class EquipoService {
    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    @Autowired
    EquipoRepository equipoRepository;

    @Transactional
    public Equipo crearEquipo(String nombre) {
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);
        return equipo;
    }
    @Transactional(readOnly = true)
    public Equipo recuperarEquipo(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        return equipo;
    }
    @Transactional(readOnly = true)
    public List<Equipo> findAllOrderedByName(){
        List<Equipo>equipos = equipoRepository.findAll();
        Collections.sort(equipos,
                (Equipo e1,Equipo e2)->e1.getNombre().compareTo(e2.getNombre())
        );
        return equipos;
    }
}