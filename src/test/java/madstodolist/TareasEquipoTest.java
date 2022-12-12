package madstodolist;

import madstodolist.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

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
}
