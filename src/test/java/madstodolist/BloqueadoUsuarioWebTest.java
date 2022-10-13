package madstodolist;
import madstodolist.authentication.ManagerUserSession;
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

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class BloqueadoUsuarioWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void bloqueoUsuario() throws Exception{
        Usuario admin = new Usuario("admin@ua");
        admin.setPassword("admin");
        admin.setAdministrador(true);
        usuarioService.registrar(admin);
        when(managerUserSession.usuarioLogeado()).thenReturn(admin.getId());
        assertTrue(usuarioService.soyAdministrador(admin.getId()));
        String url = "/registrados";
        Usuario user = new Usuario("adel@ua");
        user.setPassword("adel");
        usuarioService.registrar(user);
        this.mockMvc.perform(get(url))
                .andExpect((content().string(
                        containsString("bloquear")
                )));
    }
    @Test
    public void habilitarUsuario() throws Exception{
        Usuario admin = new Usuario("admin@ua");
        admin.setPassword("admin");
        admin.setAdministrador(true);
        usuarioService.registrar(admin);
        when(managerUserSession.usuarioLogeado()).thenReturn(admin.getId());
        assertTrue(usuarioService.soyAdministrador(admin.getId()));
        String url = "/registrados";
        Usuario user = new Usuario("adel@ua");
        user.setPassword("adel");
        user.setAcceso(false);
        usuarioService.registrar(user);
        this.mockMvc.perform(get(url))
                .andExpect((content().string(
                        containsString("habilitar")
                )));
    }
    @Test
    public void noAccesoLogin() throws Exception{
        Usuario user = new Usuario("adel@ua");
        user.setPassword("adel");
        user.setAcceso(false);
        usuarioService.registrar(user);
        this.mockMvc.perform(post("/login")
                        .param("eMail","adel@ua")
                        .param("password","adel"))
                .andExpect(content().string(containsString("El usuario esta bloqueado")));
    }
}
