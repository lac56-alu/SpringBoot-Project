package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Equipo;
import madstodolist.model.Tarea;
import madstodolist.model.TareasEquipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.TareaService;
import madstodolist.service.TareasEquipoService;
import madstodolist.service.UsuarioService;
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

        String urlPost = "/equipo/" + variables.equipoId.toString() + "/modificarTarea/" + variables.tareaEquipoId.toString();

        this.mockMvc.perform(post(urlPost)
                .param("titulo", "Modificacion Titulo")
                .param("descripcion", "Modificacion Descripcion")
                .param("fecha", "29-04-2023"));

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Modificacion Titulo"),
                        containsString("Modificacion Descripcion"),
                        containsString("2023-04-29")
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
}
