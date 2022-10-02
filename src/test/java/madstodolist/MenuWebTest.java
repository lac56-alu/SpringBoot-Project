package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuWebTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TareaService tareaService;

    @MockBean
    private ManagerUserSession managerUserSession;

    class DosIds {
        Long usuarioId;
        Long tareaId;
        public DosIds(Long usuarioId, Long tareaId) {
            this.usuarioId = usuarioId;
            this.tareaId = tareaId;
        }
    }
   DosIds addUsuarioTareasBD() {
       Usuario usuario = new Usuario("user@ua");
       usuario.setPassword("123");
       usuario = usuarioService.registrar(usuario);
       Tarea tarea1 = tareaService.nuevaTareaUsuario(usuario.getId(), "Lavar coche");
       tareaService.nuevaTareaUsuario(usuario.getId(), "Renovar DNI");
       return new DosIds(usuario.getId(), tarea1.getId());
   }
    @Test
    public void devuelveMenuLogueado()throws Exception{
        Long usuarioId = addUsuarioTareasBD().usuarioId;
        when(managerUserSession.usuarioLogeado()).thenReturn(usuarioId);

        String url = "/usuarios/" + usuarioId.toString() + "/tareas";

        this.mockMvc.perform(get(url))
                .andExpect((content().string(allOf(
                        containsString("Lavar coche"),
                        containsString("Renovar DNI"),
                        containsString("Tareas"),
                        containsString("ToDoList"),
                        containsString("Cerrar")
                ))));
    }
}
