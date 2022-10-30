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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class GestionEquipoWebTest {
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
        Equipo equipo= equipoService.crearEquipo("Equipo1");
        Equipo equipo2= equipoService.crearEquipo("Equipo2");
        equipoService.addUsuarioEquipo(us.getId(),equipo.getId());
        String url = "/equipos";

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Equipo1"),
                        containsString("Equipo2"),
                        containsString("Añadirme"),
                        containsString("Eliminarme")
                ))));
    }
    @Test
    public void crearEquipo()throws Exception{
        Usuario us = new Usuario("user@ua");
        us.setPassword("123");
        us.setAdministrador(true);
        us = usuarioService.registrar(us);
        when(managerUserSession.usuarioLogeado()).thenReturn(us.getId());
        String url = "/equipos/nuevo";
        this.mockMvc.perform(post(url)
                        .param("nombre", "EquipoNew"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos"));

        this.mockMvc.perform(get("/equipos"))
                .andExpect((content().string(allOf(
                        containsString("EquipoNew"),
                        containsString("Añadirme")
                ))));
    }
}
