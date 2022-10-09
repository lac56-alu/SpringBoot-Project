package madstodolist;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import madstodolist.service.UsuarioServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class UsuarioAdminServiceTest {
    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void crearAdministrador(){
        Usuario admin = new Usuario("admin@ua");
        admin.setPassword("admin");
        admin.setAdministrador(true);
        assertFalse(usuarioService.hayAdministrador());
        usuarioService.registrar(admin);
        assertTrue(usuarioService.hayAdministrador());
    }
}
