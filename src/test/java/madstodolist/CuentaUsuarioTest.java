package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Usuario;
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
public class CuentaUsuarioTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioService usuarioService;
    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void getInformacionCuenta() throws Exception {
        Usuario usuario = new Usuario("test@ua");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("1234");
        Usuario usuarioFinal = usuarioService.registrar(usuario);

        when(managerUserSession.usuarioLogeado()).thenReturn(usuarioFinal.getId());

        String urlPeticion = "/cuenta";

        this.mockMvc.perform(get(urlPeticion))
                .andExpect((content().string(allOf(
                        containsString("test@ua"),
                        containsString("Usuario Test"),
                        containsString("Nombre"),
                        containsString("Email"),
                        containsString("Administrador"),
                        containsString("false"),
                        containsString("Modificar Usuario"),
                        containsString("Eliminar Usuario")
                ))));
    }
}
