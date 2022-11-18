package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ListadoEquiposWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EquipoService equipoService;

    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void listaEquipos()throws Exception{
        Usuario us = new Usuario("user@ua");
        us.setPassword("123");
        us.setAdministrador(true);
        us = usuarioService.registrar(us);
        when(managerUserSession.usuarioLogeado()).thenReturn(us.getId());
        Equipo equipo= equipoService.crearEquipo("Equipo1", "Descripcion Equipo 1");
        Equipo equipo2= equipoService.crearEquipo("Equipo2", "Descripcion Equipo 2");
        equipoService.addUsuarioEquipo(us.getId(),equipo.getId());
        String url = "/equipos";

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Equipo1"),
                        containsString("Descripcion Equipo 1"),
                        containsString("Equipo2"),
                        containsString("Descripcion Equipo 2")
                ))));
    }
    @Test
    public void listaMiembros()throws Exception{
        Usuario us = new Usuario("user@ua");
        us.setPassword("123");
        us.setAdministrador(true);
        us = usuarioService.registrar(us);
        Usuario us2 = new Usuario("user2@ua");
        us2.setPassword("123");
        us2 = usuarioService.registrar(us2);
        when(managerUserSession.usuarioLogeado()).thenReturn(us.getId());
        Equipo equipo= equipoService.crearEquipo("Equipo1", "Descripcion Equipo 1");
        equipoService.addUsuarioEquipo(us.getId(),equipo.getId());
        equipoService.addUsuarioEquipo(us2.getId(),equipo.getId());
        String url = "/equipos/"+equipo.getId();

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("user@ua"),
                        containsString("user2@ua")
                ))));
    }
}
