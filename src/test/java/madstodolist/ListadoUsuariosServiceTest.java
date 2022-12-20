package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class ListadoUsuariosServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void testNuevoUser(){
        Usuario user = new Usuario("prueba01@ua");
        user.setPassword("12345");
        Usuario user2 = new Usuario("prueba02@ua");
        user2.setPassword("12345");
        usuarioService.registrar(user);
        usuarioService.registrar(user2);
        List<Usuario> users = usuarioService.findAll();


        assertEquals("prueba01@ua",users.get(0).getEmail());
        assertEquals("prueba02@ua",users.get(1).getEmail());

    }
    @Test
    public void buscarUsuario(){
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario.setAdministrador(true);
        usuarioService.registrar(usuario);
        List<Usuario>users = usuarioService.busquedaUser(null);
        assertEquals(1,users.size());
        assertEquals("user@ua",users.get(0).getEmail());
    }
}
