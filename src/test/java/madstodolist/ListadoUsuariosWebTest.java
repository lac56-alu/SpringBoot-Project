package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Equipo;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ListadoUsuariosWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void listaUsers()throws Exception{
        Usuario us = new Usuario("user@ua");
        us.setPassword("123");
        us.setAdministrador(true);
        us = usuarioService.registrar(us);
        when(managerUserSession.usuarioLogeado()).thenReturn(us.getId());
        Usuario us2 = new Usuario("prueba01@ua");
        us2.setPassword("123");
        us2 = usuarioService.registrar(us2);
        String url = "/registrados";

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("user@ua"),
                        containsString("1"),
                        containsString("prueba01@ua"),
                        containsString("2")
                ))));
    }
    @Test
    public void listaUsersBuscados()throws Exception{
        Usuario us = new Usuario("user@ua");
        us.setPassword("123");
        us.setAdministrador(true);
        us = usuarioService.registrar(us);
        Usuario us2 = new Usuario("user2@ua");
        us2.setPassword("123");
        us2 = usuarioService.registrar(us2);
        when(managerUserSession.usuarioLogeado()).thenReturn(us.getId());
        String url = "/registrados";

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Buscar"),
                        containsString("Limpiar")
                ))));
        this.mockMvc.perform(get(url+"?busca="+"user2"))
                .andExpect((content().string(allOf(
                        containsString("Buscar"),
                        containsString("Limpiar"),
                        containsString("user2")
                ))));
    }

}
