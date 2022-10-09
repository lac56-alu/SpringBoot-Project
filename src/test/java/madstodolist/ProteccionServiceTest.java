package madstodolist;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ProteccionServiceTest {
    @Autowired
    UsuarioService usuarioService;

    @Test
    public void devolverIDAdministrador(){
        Usuario admin = new Usuario("admin@ua");
        admin.setAdministrador(true);
        admin.setPassword("admin");
        usuarioService.registrar(admin);
        assertEquals(1L,usuarioService.devolverIDAdministrador());
    }
}
