package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class CuentaUsuarioTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EquipoService equipoService;
    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void getInformacionCuenta() throws Exception {
        Usuario usuario = new Usuario("test@ua");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("1234");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(formatoFecha.parse("1999-01-01"));
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
                        containsString("Fecha de nacimiento"),
                        containsString("1999-01-01"),
                        containsString("Modificar Usuario"),
                        containsString("Eliminar Usuario")
                ))));
    }

    @Test
    public void formModificarCuenta() throws Exception {
        Usuario usuario = new Usuario("test@ua");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("1234");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(formatoFecha.parse("1999-01-01"));
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
                        containsString("Fecha de nacimiento"),
                        containsString("1999-01-01"),
                        containsString("Modificar Usuario"),
                        containsString("Eliminar Usuario")
                ))));

        String urlPeticionPost = "/cuenta/modificar/" + usuarioFinal.getId().toString();

        this.mockMvc.perform(post(urlPeticionPost)
                        .param("email", "mod@ua")
                        .param("nombre", "Usuario Modificado")
                        .param("password", "1234")
                        .param("fechaNacimiento", "29-04-1999"));

        String urlPeticion2 = "/cuenta";

        this.mockMvc.perform(get(urlPeticion2))
                .andExpect((content().string(allOf(
                        containsString("mod@ua"),
                        containsString("Usuario Modificado"),
                        containsString("1999-04-29")
                ))));

    }

    @Test
    public void borrarCuenta() throws Exception {
        Usuario usuario = new Usuario("test@ua");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("1234");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(formatoFecha.parse("1999-01-01"));
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
                        containsString("Fecha de nacimiento"),
                        containsString("1999-01-01"),
                        containsString("Modificar Usuario"),
                        containsString("Eliminar Usuario")
                ))));

        String urlPeticionPost = "/cuenta/borrar/" + usuarioFinal.getId().toString();

        this.mockMvc.perform(get(urlPeticionPost))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }


    @Test
    public void verCuentaEquipos() throws Exception {
        Usuario us = new Usuario("test@ua");
        us.setNombre("Usuario Test");
        us.setPassword("1234");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        us.setFechaNacimiento(formatoFecha.parse("1999-01-01"));
        us.setPassword("123");
        us.setAdministrador(true);
        us = usuarioService.registrar(us);
        when(managerUserSession.usuarioLogeado()).thenReturn(us.getId());
        Equipo equipo= equipoService.crearEquipo("Equipo1", "Descripcion Equipo 1", us.getId());
        Equipo equipo2= equipoService.crearEquipo("Equipo2", "Descripcion Equipo 2", us.getId());
        equipoService.addUsuarioEquipo(us.getId(),equipo.getId());
        equipoService.addUsuarioEquipo(us.getId(),equipo2.getId());

        when(managerUserSession.usuarioLogeado()).thenReturn(us.getId());

        String urlPeticion = "/cuenta";

        this.mockMvc.perform(get(urlPeticion))
                .andExpect((content().string(allOf(
                        containsString("test@ua"),
                        containsString("Usuario Test"),
                        containsString("Nombre"),
                        containsString("Email"),
                        containsString("Administrador"),
                        containsString("false"),
                        containsString("Fecha de nacimiento"),
                        containsString("1999-01-01"),
                        containsString("Modificar Usuario"),
                        containsString("Eliminar Usuario"),
                        //parte equipos
                        containsString("Mis equipos"),
                        containsString("Equipo1"),
                        containsString("Equipo2"),
                        containsString("Descripcion Equipo 1"),
                        containsString("Descripcion Equipo 2"),
                        containsString("Ver miembros"),
                        containsString("Ver tareas")
                ))));
    }
}
