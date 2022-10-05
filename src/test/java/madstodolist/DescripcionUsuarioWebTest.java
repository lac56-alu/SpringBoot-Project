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

import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class DescripcionUsuarioWebTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSession managerUserSession;


    @Test
    public void testComprobarDescricpionUser() throws Exception{
        Usuario user = new Usuario("usuario@ua");
        user.setPassword("user");
        user.setNombre("user");
        Date fecha = new Date(2000-1900,03,17);
        user.setFechaNacimiento(fecha);
        user = usuarioService.registrar(user);

        String url = "/registrados/"+user.getId();
        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Descripcion"),
                        containsString("ID"),
                        containsString("Correo electronico"),
                        containsString("Nombre"),
                        containsString("Fecha de Nacimiento"),
                        containsString("1"),
                        containsString("user"),
                        containsString("usuario@ua"),
                        containsString("2000-04-17")
                ))));

    }
}
