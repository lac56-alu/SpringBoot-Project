package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Equipo;
import madstodolist.model.Tarea;
import madstodolist.model.TareasEquipo;
import madstodolist.model.Usuario;
import madstodolist.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareasEquipoWebTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TareasEquipoService tareasEquipoService;
    @Autowired
    private EquipoService equipoService;
    @Autowired
    private UsuarioService usuarioService;
    @MockBean
    private ManagerUserSession managerUserSession;

    class DosIds {
        Long usuarioId;
        Long equipoId;
        Long tareaEquipoId;
        public DosIds(Long usuarioId, Long equipoId, Long tareaEquipoId) {
            this.usuarioId = usuarioId;
            this.equipoId = equipoId;
            this.tareaEquipoId = tareaEquipoId;
        }
    }

    TareasEquipoWebTest.DosIds addTareasEquipoBD() {
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo equipo = equipoService.crearEquipo("Equipo", "Descripcion", usuario.getId());
        equipo.setLider(usuario.getId());
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatoFecha.parse("2022-12-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TareasEquipo tareasEquipo = tareasEquipoService.nuevaTareaEquipo(equipo.getId(), "Titulo 1", "Descripcion 1", fecha);
        TareasEquipo tareasEquipo2 = tareasEquipoService.nuevaTareaEquipo(equipo.getId(), "Titulo 2", "Descripcion 2", fecha);
        return new TareasEquipoWebTest.DosIds(usuario.getId(),equipo.getId(), tareasEquipo.getId());
    }

    @Test
    public void listaTareas() throws Exception {
        TareasEquipoWebTest.DosIds variables = addTareasEquipoBD();

        when(managerUserSession.usuarioLogeado()).thenReturn(variables.usuarioId);

        String url = "/equipo/" + variables.equipoId.toString() + "/listaTareas";

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Titulo 1"),
                        containsString("Descripcion 1"),
                        containsString("2022-12-12"),
                        containsString("Titulo 2"),
                        containsString("Descripcion 2"),
                        containsString("2022-12-12")
                ))));
    }

    @Test
    public void comprobarModificacionTareaEquipo() throws Exception {
        TareasEquipoWebTest.DosIds variables = addTareasEquipoBD();

        when(managerUserSession.usuarioLogeado()).thenReturn(variables.usuarioId);

        String url = "/equipo/" + variables.equipoId.toString() + "/listaTareas";

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Titulo 1"),
                        containsString("Descripcion 1"),
                        containsString("2022-12-12")
                ))));

        TareasEquipo tarea = tareasEquipoService.findById(variables.tareaEquipoId);
        tarea.setTitulo("Modificacion Titulo");
        tarea.setDescripcion("Modificacion Descripcion");
        tareasEquipoService.guardarTareaEquipo(tarea);

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Modificacion Titulo"),
                        containsString("Modificacion Descripcion")
                ))));
    }

    @Test
    public void deleteTareaEquipo() throws Exception {
        TareasEquipoWebTest.DosIds variables = addTareasEquipoBD();

        when(managerUserSession.usuarioLogeado()).thenReturn(variables.usuarioId);

        String urlDelete = "/equipo/" + variables.equipoId.toString() + "/borrarTarea/" + variables.tareaEquipoId.toString();

        this.mockMvc.perform(delete(urlDelete));

        String url = "/equipo/" + variables.equipoId.toString() + "/listaTareas";

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        not(containsString("Titulo 1")),
                        not(containsString("Descripcion 1"))))));
    }

    @Test
    public void crearTareaEquipo() throws Exception {
        TareasEquipoWebTest.DosIds variables = addTareasEquipoBD();

        when(managerUserSession.usuarioLogeado()).thenReturn(variables.usuarioId);

        Date fecha = new Date();
        tareasEquipoService.nuevaTareaEquipo(variables.equipoId,"Crear Titulo","Crear Descripcion",fecha);
        String url = "/equipo/" + variables.equipoId.toString() + "/listaTareas";
        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Crear Titulo"),
                        containsString("Crear Descripcion")
                ))));
    }

    @Test
    public void crearTareaEquipo2() throws Exception {
        TareasEquipoWebTest.DosIds variables = addTareasEquipoBD();

        when(managerUserSession.usuarioLogeado()).thenReturn(variables.usuarioId);

        Date fecha = new Date();
        tareasEquipoService.nuevaTareaEquipo(variables.equipoId,"Crear Titulo","Crear Descripcion",fecha);
        String url = "/equipo/" + variables.equipoId.toString() + "/listaTareas";
        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Crear Titulo"),
                        containsString("Crear Descripcion"),
                        containsString("POR_HACER")

                ))));
    }

    @Test
    public void comprobarModificacionEstadoTareaEquipo() throws Exception {
        TareasEquipoWebTest.DosIds variables = addTareasEquipoBD();

        when(managerUserSession.usuarioLogeado()).thenReturn(variables.usuarioId);

        String url = "/equipo/" + variables.equipoId.toString() + "/listaTareas";

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Titulo 1"),
                        containsString("Descripcion 1"),
                        containsString("2022-12-12"),
                        containsString("POR_HACER")
                ))));

        tareasEquipoService.modificarEstadoTareaEquipo(variables.tareaEquipoId, "EN_PROCESO");

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Titulo 1"),
                        containsString("Descripcion 1"),
                        containsString("2022-12-12"),
                        containsString("EN_PROCESO")
                ))));

        tareasEquipoService.modificarEstadoTareaEquipo(variables.tareaEquipoId, "FINALIZADA");

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Titulo 1"),
                        containsString("Descripcion 1"),
                        containsString("2022-12-12"),
                        containsString("FINALIZADA")
                ))));
    }

    @Test
    public void comprobarBotones() throws Exception {
        TareasEquipoWebTest.DosIds variables = addTareasEquipoBD();

        when(managerUserSession.usuarioLogeado()).thenReturn(variables.usuarioId);

        String url = "/equipo/" + variables.equipoId.toString() + "/modificarEstado/" + variables.tareaEquipoId.toString();

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Modificar"),
                        containsString("Cancelar")
                ))));
    }
}
