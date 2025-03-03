package madstodolist;

import madstodolist.controller.exception.EquipoNoNameException;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class EquipoServiceTest {

    @Autowired
    EquipoService equipoService;
    @Autowired
    UsuarioService usuarioService;

    @Test
    public void crearRecuperarEquipo() {
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo equipo = equipoService.crearEquipo("Proyecto 1", "Descripcion Proyecto 1", usuario.getId());
        Equipo equipoBd = equipoService.recuperarEquipo(equipo.getId());
        assertThat(equipoBd).isNotNull();
        assertThat(equipoBd.getNombre()).isEqualTo("Proyecto 1");
    }
    @Test
    public void listadoEquiposOrdenAlfabetico() {
        // GIVEN
        // Dos equipos en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        equipoService.crearEquipo("Proyecto BBB", "Descripcion Proyecto BBB", usuario.getId());
        equipoService.crearEquipo("Proyecto AAA", "Descripcion Proyecto AAA", usuario.getId());

        // WHEN
        // Recuperamos los equipos
        List<Equipo> equipos = equipoService.findAllOrderedByName();

        // THEN
        // Los equipos están ordenados por nombre
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Proyecto AAA");
        assertThat(equipos.get(0).getDescripcion()).isEqualTo("Descripcion Proyecto AAA");
        assertThat(equipos.get(1).getNombre()).isEqualTo("Proyecto BBB");
        assertThat(equipos.get(1).getDescripcion()).isEqualTo("Descripcion Proyecto BBB");
    }
    @Test
    public void accesoUsuariosGeneraExcepcion() {
        // Given
        // Un equipo en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo equipo = equipoService.crearEquipo("Proyecto 1", "Descripcion Proyecto 1", usuario.getId());

        // WHEN
        // Se recupera el equipo
        Equipo equipoBd = equipoService.recuperarEquipo(equipo.getId());

        // THEN
        // Se produce una excepción al intentar acceder a sus usuarios
        assertThatThrownBy(() -> {
            equipoBd.getUsuarios().size();
        }).isInstanceOf(LazyInitializationException.class);
    }
    @Test
    public void actualizarRecuperarUsuarioEquipo() {
        // GIVEN
        // Un equipo creado en la base de datos y un usuario registrado
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo equipo = equipoService.crearEquipo("Proyecto 1", "Descripcion Proyecto 1", usuario.getId());

        // WHEN
        // Añadimos el usuario al equipo y lo recuperamos
        equipoService.addUsuarioEquipo(usuario.getId(), equipo.getId());
        List<Usuario> usuarios = equipoService.usuariosEquipo(equipo.getId());

        // THEN
        // El usuario se ha recuperado correctamente
        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("user@ua");
    }
    @Test
    public void comprobarRelacionUsuarioEquipos() {
        // GIVEN
        // Un equipo creado en la base de datos y un usuario registrado
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo equipo = equipoService.crearEquipo("Proyecto 1", "Descripcion Proyecto 1", usuario.getId());

        // WHEN
        // Añadimos el usuario al equipo y lo recuperamos
        equipoService.addUsuarioEquipo(usuario.getId(), equipo.getId());
        Usuario usuarioBD = usuarioService.findById(usuario.getId());

        // THEN
        // Se recuperan también los equipos del usuario,
        // porque la relación entre usuarios y equipos es EAGER
        assertThat(usuarioBD.getEquipos()).hasSize(1);
    }
    @Test
    public void eliminarUsuarioEquipo() {
        // GIVEN
        // Un equipo creado en la base de datos y un usuario registrado
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo equipo = equipoService.crearEquipo("Proyecto 1", "Descripcion Proyecto 1", usuario.getId());
        // WHEN
        // Añadimos el usuario al equipo y lo recuperamos
        equipoService.addUsuarioEquipo(usuario.getId(), equipo.getId());
        List<Usuario> usuarios = equipoService.usuariosEquipo(equipo.getId());

        // THEN
        // El usuario se ha recuperado correctamente
        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("user@ua");

        equipoService.deleteUsuarioEquipo(usuario.getId(),equipo.getId());
        usuarios = equipoService.usuariosEquipo(equipo.getId());

        // THEN
        // El usuario se ha eliminado correctamente
        assertThat(usuarios).hasSize(0);
    }
    @Test
    public void noNombreEquipoException(){
        Usuario usuario = new Usuario("user@ua");
        assertThatThrownBy(() -> {
            equipoService.crearEquipo("", "", usuario.getId());
        }).isInstanceOf(EquipoNoNameException.class);
    }
    @Test
    public void eliminarEquipo(){
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo eq = equipoService.crearEquipo("Equipo", "Descripcion Equipo", usuario.getId());
        Equipo EB= equipoService.recuperarEquipo(eq.getId());
        assertThat(EB).isNotNull();
        equipoService.eliminarEquipo(eq);
        assertNull(equipoService.recuperarEquipo(eq.getId()));
    }
    @Test
    public void modificarEquipo(){
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo eq = equipoService.crearEquipo("Equipo", "Descripcion Equipo", usuario.getId());
        Equipo EB= equipoService.recuperarEquipo(eq.getId());
        assertThat(EB).isNotNull();
        equipoService.modificarEquipo(eq,"NuevoEquipo", "NuevaDescripcion");
        assertEquals("NuevoEquipo",eq.getNombre());
    }
    @Test
    public void buscarEquipo(){
        Usuario usuario = new Usuario("user@ua");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        Equipo eq = equipoService.crearEquipo("Equipo01", "Descripcion Equipo", usuario.getId());
        Equipo EB= equipoService.recuperarEquipo(eq.getId());
        assertThat(EB).isNotNull();
        List<Equipo>equipos = equipoService.buscador("Equipo");
        assertEquals(1,equipos.size());
        assertEquals("Equipo01",equipos.get(0).getNombre());
    }

}