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
public class ProteccionListadoDescripcionWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSession managerUserSession;

    class ID {
        Long usuarioId;
        public ID(Long usuarioId) {
            this.usuarioId = usuarioId;
        }
    }
    ID addUsuarioBD() {
        Usuario user = new Usuario("admi@ua");
        user.setPassword("123");
        user.setNombre("adel");
        user.setAdministrador(true);
        user = usuarioService.registrar(user);
        return new ID(user.getId());
    }
    @Test
    public void accederRegistradosSiendoAdminTest() throws Exception{
        Usuario admin = new Usuario("admin@ua");
        admin.setPassword("admin");
        admin.setAdministrador(true);
        usuarioService.registrar(admin);
        assertTrue(usuarioService.hayAdministrador());
        when(managerUserSession.usuarioLogeado()).thenReturn(admin.getId());
        assertTrue(usuarioService.soyAdministrador(admin.getId()));
        String url = "/registrados";
        this.mockMvc.perform(get(url))
                .andExpect((content().string(
                        containsString("Listado")
                )));
    }
    @Test
    public void accederRegistradosSiendoUsuarioNormalTest() throws Exception{
        Usuario user = new Usuario("user@ua");
        user.setPassword("123");
        usuarioService.registrar(user);
        assertFalse(usuarioService.hayAdministrador());
        when(managerUserSession.usuarioLogeado()).thenReturn(user.getId());
        assertFalse(usuarioService.soyAdministrador(user.getId()));
        String url = "/registrados";
        this.mockMvc.perform(get(url))
                .andExpect((status().isUnauthorized()
                ));
    }
    @Test
    public void accederDescripcionSiendoAdminTest() throws Exception{
        Long id = addUsuarioBD().usuarioId;
        assertTrue(usuarioService.hayAdministrador());
        when(managerUserSession.usuarioLogeado()).thenReturn(id);
        assertTrue(usuarioService.soyAdministrador(id));
        String url = "/registrados/"+id;
        this.mockMvc.perform(get(url))
                .andExpect((content().string(
                        containsString("Descripcion")
                )));
    }
    @Test
    public void accederDescripcionSiendoUsuarioNormalTest() throws Exception{
        Usuario user = new Usuario("user@ua");
        user.setPassword("123");
        usuarioService.registrar(user);
        assertFalse(usuarioService.hayAdministrador());
        when(managerUserSession.usuarioLogeado()).thenReturn(user.getId());
        assertFalse(usuarioService.soyAdministrador(user.getId()));
        String url = "/registrados/1";
        this.mockMvc.perform(get(url))
                .andExpect((status().isUnauthorized()
                ));
    }
}
