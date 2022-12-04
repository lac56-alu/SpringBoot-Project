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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class UsuarioAdminWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void apareceCheckBoxTest() throws Exception{
        String url = "/registro";
        this.mockMvc.perform(get(url))
                .andExpect((content().string(
                        containsString("Ser Administrador")
                )));
    }
    @Test
    public void noApareceCheckBoxTest()throws Exception{
        Usuario admin = new Usuario("admin@ua");
        admin.setPassword("admin");
        admin.setAdministrador(true);
        usuarioService.registrar(admin);
        String url = "/registro";
        this.mockMvc.perform(get(url))
                .andExpect((content().string(
                        (not(containsString("Ser Administrador")
                )))));
    }
    @Test
    public void accedeAdminRegistrados()throws Exception{
        Usuario admin = new Usuario("admin@ua");
        admin.setPassword("admin");
        admin.setAdministrador(true);
        admin.setEnabled(true);
        usuarioService.registrar(admin);
        String url = "/registro";
        this.mockMvc.perform(post("/login")
                        .param("eMail", "admin@ua")
                        .param("password", "admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registrados"));
    }
}
