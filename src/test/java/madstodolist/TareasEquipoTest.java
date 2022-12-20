package madstodolist;

import madstodolist.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareasEquipoTest {
    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    TareasEquipoRepository tareasEquipoRepositoryRepository;

    @Test
    public void crearTarea() {
        Equipo equipo = new Equipo("Equipo");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareasEquipo = new TareasEquipo(equipo, "Titulo", "Descripcion", fecha);


        assertThat(tareasEquipo.getTitulo()).isEqualTo("Titulo");
        assertThat(tareasEquipo.getDescripcion()).isEqualTo("Descripcion");
        assertThat(tareasEquipo.getFecha()).isEqualTo(fecha);
        assertThat(tareasEquipo.getEquipo()).isEqualTo(equipo);
    }

    @Test
    public void comprobarIgualdadTareasSinId() {
        Equipo equipo = new Equipo("Equipo");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareasEquipo = new TareasEquipo(equipo, "Titulo", "Descripcion", fecha);
        TareasEquipo tareasEquipo2 = new TareasEquipo(equipo, "Titulo", "Descripcion", fecha);
        TareasEquipo tareasEquipo3 = new TareasEquipo(equipo, "Cambio", "Cambio", fecha);


        assertThat(tareasEquipo).isEqualTo(tareasEquipo2);
        assertThat(tareasEquipo).isNotEqualTo(tareasEquipo3);
    }

    @Test
    public void laListaDeTareasDeUnEquipoSeActualizaEnMemoriaConUnaNuevaTarea() {
        Equipo equipo = new Equipo("Equipo");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareasEquipo = new TareasEquipo(equipo, "Titulo", "Descripcion", fecha);
        TareasEquipo tareasEquipo2 = new TareasEquipo(equipo, "Titulo", "Descripcion", fecha);

        Set<TareasEquipo> tareas = equipo.getTareas();

        assertThat(equipo.getTareas()).contains(tareasEquipo);
        assertThat(equipo.getTareas()).contains(tareasEquipo2);
        assertThat(tareas).contains(tareasEquipo);
        assertThat(tareas).contains(tareasEquipo2);
    }

    @Test
    public void comprobarIgualdadTareasConId() {
        Equipo equipo = new Equipo("Equipo");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareasEquipo = new TareasEquipo(equipo, "Titulo 1", "Descripcion 1", fecha);
        TareasEquipo tareasEquipo2 = new TareasEquipo(equipo, "Titulo 2", "Descripcion 2", fecha);
        TareasEquipo tareasEquipo3 = new TareasEquipo(equipo, "Titulo 3", "Descripcion 3", fecha);
        tareasEquipo.setId(1L);
        tareasEquipo2.setId(2L);
        tareasEquipo3.setId(1L);

        assertThat(tareasEquipo).isEqualTo(tareasEquipo3);
        assertThat(tareasEquipo).isNotEqualTo(tareasEquipo2);
    }

    @Test
    @Transactional
    public void guardarTareaEnBaseDatos() {
        Equipo equipo = new Equipo("Equipo 2");
        equipoRepository.save(equipo);

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareaEquipo = new TareasEquipo(equipo, "Titulo 1", "Descripcion 1", fecha);
        tareasEquipoRepositoryRepository.save(tareaEquipo);

        assertThat(tareaEquipo.getId()).isNotNull();

        TareasEquipo tareaEquipoBD = tareasEquipoRepositoryRepository.findById(tareaEquipo.getId()).orElse(null);
        assertThat(tareaEquipoBD.getTitulo()).isEqualTo(tareaEquipo.getTitulo());
        assertThat(tareaEquipoBD.getEquipo()).isEqualTo(equipo);
    }

    @Test
    @Transactional
    public void unEquipoTieneUnaListaDeTareas() {
        Equipo equipo = new Equipo("Equipo 99");
        equipoRepository.save(equipo);
        Long equipoId = equipo.getId();

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareaEquipo = new TareasEquipo(equipo, "Titulo 1", "Descripcion 1", fecha);
        TareasEquipo tareaEquipo2 = new TareasEquipo(equipo, "Titulo 2", "Descripcion 2", fecha);
        tareasEquipoRepositoryRepository.save(tareaEquipo);
        tareasEquipoRepositoryRepository.save(tareaEquipo2);

        Equipo equipoRecuperado = equipoRepository.findById(equipoId).orElse(null);

        assertThat(equipoRecuperado.getTareas()).hasSize(2);
    }

    @Test
    @Transactional
    public void añadirUnaTareaAUnEquipoEnBD() {
        Equipo equipo = new Equipo("Equipo 99");
        equipoRepository.save(equipo);
        Long equipoId = equipo.getId();

        Equipo equipoBD = equipoRepository.findById(equipoId).orElse(null);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareaEquipo = new TareasEquipo(equipo, "Titulo 1", "Descripcion 1", fecha);
        tareasEquipoRepositoryRepository.save(tareaEquipo);
        Long tareaId = tareaEquipo.getId();

        TareasEquipo tareaEquipoBD = tareasEquipoRepositoryRepository.findById(tareaId).orElse(null);
        assertThat(tareaEquipoBD).isEqualTo(tareaEquipo);
        assertThat(tareaEquipo.getEquipo().getId()).isEqualTo(equipoBD.getId());

        // y si recuperamos el usuario se obtiene la nueva tarea
        equipoBD = equipoRepository.findById(equipoId).orElse(null);
        assertThat(equipoBD.getTareas()).contains(tareaEquipoBD);
    }

    @Test
    @Transactional
    public void cambioEnLaEntidadEnTransactionalModificaLaBD() {
        Equipo equipo = new Equipo("Equipo 99");
        equipoRepository.save(equipo);

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareaEquipo = new TareasEquipo(equipo, "Titulo 1", "Descripcion 1", fecha);
        tareasEquipoRepositoryRepository.save(tareaEquipo);

        Long tareaId = tareaEquipo.getId();
        tareaEquipo = tareasEquipoRepositoryRepository.findById(tareaId).orElse(null);

        tareaEquipo.setTitulo("Prueba");

        TareasEquipo tareaBD = tareasEquipoRepositoryRepository.findById(tareaId).orElse(null);
        assertThat(tareaBD.getTitulo()).isEqualTo(tareaEquipo.getTitulo());
    }
}
