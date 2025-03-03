package madstodolist;


import madstodolist.model.Tarea;
import madstodolist.model.TareaRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TareaRepository tareaRepository;

    //
    // Tests modelo Tarea en memoria, sin la conexión con la BD
    //
    @Test
    public void crearTarea() {
        // GIVEN
        // Un usuario nuevo creado en memoria, sin conexión con la BD,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una nueva tarea con ese usuario,
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", null);

        // THEN
        // el título y el usuario de la tarea son los correctos.

        assertThat(tarea.getTitulo()).isEqualTo("Práctica 1 de MADS");
        assertThat(tarea.getUsuario()).isEqualTo(usuario);
    }
    @Test
    public void crearTareaConFecha() {
        // GIVEN
        // Un usuario nuevo creado en memoria, sin conexión con la BD,
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una nueva tarea con ese usuario y fecha,
        LocalDate date = LocalDate.parse("2022-12-20");
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", date);

        // THEN
        assertThat(tarea.getFechaFinal()).isEqualTo(LocalDate.parse("2022-12-20"));
    }

    @Test
    public void laListaDeTareasDeUnUsuarioSeActualizaEnMemoriaConUnaNuevaTarea() {
        // GIVEN
        // Un usuario nuevo creado en memoria, sin conexión con la BD,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una tarea de ese usuario,

        Set<Tarea> tareas = usuario.getTareas();
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", null);

        // THEN
        // la tarea creada se ha añadido a la lista de tareas del usuario.

        assertThat(usuario.getTareas()).contains(tarea);
        assertThat(tareas).contains(tarea);
    }

    @Test
    public void comprobarIgualdadTareasSinId() {
        // GIVEN
        // Creadas tres tareas sin identificador, y dos de ellas con
        // la misma descripción

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS", null);
        Tarea tarea2 = new Tarea(usuario, "Práctica 1 de MADS", null);
        Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler", null);

        // THEN
        // son iguales (Equal) las tareas que tienen la misma descripción.

        assertThat(tarea1).isEqualTo(tarea2);
        assertThat(tarea1).isNotEqualTo(tarea3);
    }

    @Test
    public void comprobarIgualdadTareasConId() {
        // GIVEN
        // Creadas tres tareas con distintas descripciones y dos de ellas
        // con el mismo identificador,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS", null);
        Tarea tarea2 = new Tarea(usuario, "Lavar la ropa", null);
        Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler", null);
        tarea1.setId(1L);
        tarea2.setId(2L);
        tarea3.setId(1L);

        // THEN
        // son iguales (Equal) las tareas que tienen el mismo identificador.

        assertThat(tarea1).isEqualTo(tarea3);
        assertThat(tarea1).isNotEqualTo(tarea2);
    }

    //
    // Tests TareaRepository.
    // El código que trabaja con repositorios debe
    // estar en un entorno transactional, para que todas las peticiones
    // estén en la misma conexión a la base de datos, las entidades estén
    // conectadas y sea posible acceder a colecciones LAZY.
    //

    @Test
    @Transactional
    public void guardarTareaEnBaseDatos() {
        // GIVEN
        // Un usuario en la base de datos.

        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);

        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", null);

        // WHEN
        // salvamos la tarea en la BD,

        tareaRepository.save(tarea);

        // THEN
        // se actualiza el id de la tarea,

        assertThat(tarea.getId()).isNotNull();

        // y con ese identificador se recupera de la base de datos la tarea
        // con los valores correctos de las propiedades y la relación con
        // el usuario actualizado también correctamente (la relación entre tarea
        // y usuario es EAGER).

        Tarea tareaBD = tareaRepository.findById(tarea.getId()).orElse(null);
        assertThat(tareaBD.getTitulo()).isEqualTo(tarea.getTitulo());
        assertThat(tareaBD.getUsuario()).isEqualTo(usuario);
    }

    @Test
    @Transactional
    public void salvarTareaEnBaseDatosConUsuarioNoBDLanzaExcepcion() {
        // GIVEN
        // Un usuario nuevo que no está en la BD
        // y una tarea asociada a ese usuario,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", null);

        // WHEN // THEN
        // se lanza una excepción al intentar salvar la tarea en la BD

        Assertions.assertThrows(Exception.class, () -> {
            tareaRepository.save(tarea);
        });
    }

    @Test
    @Transactional
    public void unUsuarioTieneUnaListaDeTareas() {
        // GIVEN
        // Un usuario con 2 tareas en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);
        Long usuarioId = usuario.getId();

        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS", null);
        Tarea tarea2 = new Tarea(usuario, "Renovar el DNI", null);
        tareaRepository.save(tarea1);
        tareaRepository.save(tarea2);

        // WHEN
        // recuperamos el ususario de la base de datos,

        Usuario usuarioRecuperado = usuarioRepository.findById(usuarioId).orElse(null);

        // THEN
        // su lista de tareas también se recupera, porque se ha
        // definido la relación de usuario y tareas como EAGER.

        assertThat(usuarioRecuperado.getTareas()).hasSize(2);
    }

    @Test
    @Transactional
    public void añadirUnaTareaAUnUsuarioEnBD() {
        // GIVEN
        // Un usuario en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);
        Long usuarioId = usuario.getId();

        // WHEN
        // Creamos una nueva tarea con el usuario recuperado de la BD
        // y la salvamos,

        Usuario usuarioBD = usuarioRepository.findById(usuarioId).orElse(null);
        Tarea tarea = new Tarea(usuarioBD, "Práctica 1 de MADS", null);
        tareaRepository.save(tarea);
        Long tareaId = tarea.getId();

        // THEN
        // la tarea queda guardada en la BD asociada al usuario

        Tarea tareaBD = tareaRepository.findById(tareaId).orElse(null);
        assertThat(tareaBD).isEqualTo(tarea);
        assertThat(tarea.getUsuario()).isEqualTo(usuarioBD);

        // y si recuperamos el usuario se obtiene la nueva tarea
        usuarioBD = usuarioRepository.findById(usuarioId).orElse(null);
        assertThat(usuarioBD.getTareas()).contains(tareaBD);
    }


    @Test
    @Transactional
    public void cambioEnLaEntidadEnTransactionalModificaLaBD() {
        // GIVEN
        // Un usuario y una tarea en la base de datos
        Usuario usuario = new Usuario("user@ua");
        usuarioRepository.save(usuario);
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", null);
        tareaRepository.save(tarea);

        // Recuperamos la tarea
        Long tareaId = tarea.getId();
        tarea = tareaRepository.findById(tareaId).orElse(null);

        // WHEN
        // modificamos la descripción de la tarea

        tarea.setTitulo("Esto es una prueba");

        // THEN
        // la descripción queda actualizada en la BD.

        Tarea tareaBD = tareaRepository.findById(tareaId).orElse(null);
        assertThat(tareaBD.getTitulo()).isEqualTo(tarea.getTitulo());
    }
}
