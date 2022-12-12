package madstodolist;

import madstodolist.model.*;
import madstodolist.service.EquipoService;
import madstodolist.service.TareaService;
import madstodolist.service.TareasEquipoService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareasEquipoServiceTest {
    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareasEquipoService tareasEquipoService;

    class DosIds {
        Long equipoId;
        Long tareaEquipoId;
        public DosIds(Long equipoId, Long tareaEquipoId) {
            this.equipoId = equipoId;
            this.tareaEquipoId = tareaEquipoId;
        }
    }

    TareasEquipoServiceTest.DosIds addTareasEquipoBD() {
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo equipo = equipoService.crearEquipo("Equipo", "Descripcion", usuario.getId());

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareasEquipo = tareasEquipoService.nuevaTareaEquipo(equipo.getId(), "Titulo 1", "Descripcion 1", fecha);
        return new TareasEquipoServiceTest.DosIds(equipo.getId(), tareasEquipo.getId());
    }


    @Test
    public void testNuevaTareaUsuario() {
        Long equipoId = addTareasEquipoBD().equipoId;

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareasEquipo = tareasEquipoService.nuevaTareaEquipo(equipoId,
                "Titulo Prueba", "Descripcion Prueba", fecha);

        Equipo equipo = equipoService.findById(equipoId);
        assertThat(equipo.getTareas()).hasSize(2);
        assertThat(equipo.getTareas()).contains(tareasEquipo);
    }

    @Test
    public void testBuscarTarea() {
        Long tareaId = addTareasEquipoBD().tareaEquipoId;
        TareasEquipo tarea = tareasEquipoService.findById(tareaId);

        assertThat(tarea).isNotNull();
        assertThat(tarea.getTitulo()).isEqualTo("Titulo 1");
    }

    @Test
    public void testModificarTarea() {
        TareasEquipoServiceTest.DosIds dosIds = addTareasEquipoBD();
        Long equipoId = dosIds.equipoId;
        Long tareaEquipoId = dosIds.tareaEquipoId;
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-13");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        tareasEquipoService.modificarTareaEquipo(tareaEquipoId, "Cambio Titulo", "Cambio Descripcion", fecha);

        TareasEquipo tareaEquipoBD = tareasEquipoService.findById(tareaEquipoId);
        assertThat(tareaEquipoBD.getTitulo()).isEqualTo("Cambio Titulo");
        assertThat(tareaEquipoBD.getDescripcion()).isEqualTo("Cambio Descripcion");
        assertThat(tareaEquipoBD.getFecha()).isEqualTo(fecha);

        Equipo equipoBD = equipoService.findById(equipoId);
        equipoBD.getTareas().contains(tareaEquipoBD);
    }

    @Test
    public void testBorrarTarea() {
        TareasEquipoServiceTest.DosIds dosIds = addTareasEquipoBD();
        Long equipoId = dosIds.equipoId;
        Long tareaEquipoId = dosIds.tareaEquipoId;

        tareasEquipoService.borraTareaEquipo(tareaEquipoId);

        // THEN
        // la tarea ya no está en la base de datos ni en las tareas del usuario.

        assertThat(tareasEquipoService.findById(tareaEquipoId)).isNull();
        assertThat(equipoService.findById(equipoId).getTareas()).hasSize(0);
    }

    @Test
    public void buscarTareas(){
        TareasEquipoServiceTest.DosIds dosIds = addTareasEquipoBD();
        Long equipoId = dosIds.equipoId;
        Long tareaEquipoId = dosIds.tareaEquipoId;


        List<TareasEquipo> tareas = tareasEquipoService.allTareasEquipo(equipoId,"1");
        assertEquals(1,tareas.size());
        assertEquals("Titulo 1",tareas.get(0).getTitulo());
    }
}
