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
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    // Método para inicializar los datos de prueba en la BD
    // Devuelve el identificador del usuario de la BD
    Long addUsuarioBD() {
        Usuario usuario = new Usuario("user@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setPassword("123");
        usuario.setEnabled(true);
        usuario = usuarioService.registrar(usuario);
        return usuario.getId();
    }

    @Test
    public void servicioLoginUsuario() {
        // GIVEN
        // Un usuario en la BD

        addUsuarioBD();

        // WHEN
        // intentamos logear un usuario y contraseña correctos
        UsuarioService.LoginStatus loginStatus1 = usuarioService.login("user@ua", "123");

        // intentamos logear un usuario correcto, con una contraseña incorrecta
        UsuarioService.LoginStatus loginStatus2 = usuarioService.login("user@ua", "000");

        // intentamos logear un usuario que no existe,
        UsuarioService.LoginStatus loginStatus3 = usuarioService.login("pepito.perez@gmail.com", "12345678");

        // THEN

        // el valor devuelto por el primer login es LOGIN_OK,
        assertThat(loginStatus1).isEqualTo(UsuarioService.LoginStatus.LOGIN_OK);

        // el valor devuelto por el segundo login es ERROR_PASSWORD,
        assertThat(loginStatus2).isEqualTo(UsuarioService.LoginStatus.ERROR_PASSWORD);

        // y el valor devuelto por el tercer login es USER_NOT_FOUND.
        assertThat(loginStatus3).isEqualTo(UsuarioService.LoginStatus.USER_NOT_FOUND);
    }

    @Test
    public void servicioRegistroUsuario() {
        // GIVEN
        // Creado un usuario nuevo, con una contraseña

        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");

        // WHEN
        // registramos el usuario,

        usuarioService.registrar(usuario);

        // THEN
        // el usuario se añade correctamente al sistema.

        Usuario usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());
    }

    @Test
    public void servicioRegistroUsuarioExcepcionConNullPassword() {
        // GIVEN
        // Un usuario creado sin contraseña,

        Usuario usuario =  new Usuario("usuario.prueba@gmail.com");

        // WHEN, THEN
        // intentamos registrarlo, se produce una excepción de tipo UsuarioServiceException
        Assertions.assertThrows(UsuarioServiceException.class, () -> {
            usuarioService.registrar(usuario);
        });
    }


    @Test
    public void servicioRegistroUsuarioExcepcionConEmailRepetido() {
        // GIVEN
        // Un usuario en la BD

        addUsuarioBD();

        // WHEN
        // Creamos un usuario con un e-mail ya existente en la base de datos,
        Usuario usuario =  new Usuario("user@ua");
        usuario.setPassword("12345678");

        // THEN
        // si lo registramos, se produce una excepción de tipo UsuarioServiceException
        Assertions.assertThrows(UsuarioServiceException.class, () -> {
            usuarioService.registrar(usuario);
        });
    }

    @Test
    public void servicioRegistroUsuarioDevuelveUsuarioConId() {
        // GIVEN
        // Dado un usuario con contraseña nuevo y sin identificador,

        Usuario usuario = new Usuario("usuario.prueba@gmail.com");
        usuario.setPassword("12345678");

        // WHEN
        // lo registramos en el sistema,

        usuarioService.registrar(usuario);

        // THEN
        // se actualiza el identificador del usuario

        assertThat(usuario.getId()).isNotNull();

        // con el identificador que se ha guardado en la BD.

        Usuario usuarioBD = usuarioService.findById(usuario.getId());
        assertThat(usuarioBD).isEqualTo(usuario);
    }

    @Test
    public void servicioConsultaUsuarioDevuelveUsuario() {
        // GIVEN
        // Un usuario en la BD

        Long usuarioId = addUsuarioBD();

        // WHEN
        // recuperamos un usuario usando su e-mail,

        Usuario usuario = usuarioService.findByEmail("user@ua");

        // THEN
        // el usuario obtenido es el correcto.

        assertThat(usuario.getId()).isEqualTo(usuarioId);
        assertThat(usuario.getEmail()).isEqualTo("user@ua");
        assertThat(usuario.getNombre()).isEqualTo("Usuario Ejemplo");
    }

    @Test
    public void modificarUsuarioTest(){
        Long idUsuarioModificar = addUsuarioBD();

        Usuario usuario = new Usuario("modificar@gmail.com");
        usuario.setNombre("Modificar");
        usuario.setPassword("1234");
        usuario = usuarioService.registrar(usuario);

        usuarioService.modificarUsuario(idUsuarioModificar, usuario);

        Usuario usuarioModificado = usuarioService.findById(idUsuarioModificar);

        assertThat(usuarioModificado.getEmail()).isEqualTo("modificar@gmail.com");
        assertThat(usuarioModificado.getNombre()).isEqualTo("Modificar");
        assertThat(usuarioModificado.getPassword()).isEqualTo("1234");
    }

    @Test
    public void borrarUsuarioTest(){
        Long idUsuarioBorrar = addUsuarioBD();

        usuarioService.borrarUsuario(idUsuarioBorrar, idUsuarioBorrar);
        try{
            usuarioService.borrarUsuario(idUsuarioBorrar, idUsuarioBorrar+1);
        }
        catch(Exception exception){
            assertThat(exception.getMessage()).isEqualTo("No se puede borrar un usuario que no sea propio...");
        }

        Usuario usuarioBorrado = usuarioService.findById(idUsuarioBorrar);

        assertThat(usuarioBorrado).isNull();
    }
}