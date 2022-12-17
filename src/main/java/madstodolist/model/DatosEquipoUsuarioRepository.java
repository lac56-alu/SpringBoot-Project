package madstodolist.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DatosEquipoUsuarioRepository extends CrudRepository<DatosEquipoUsuario, Long> {
    @Modifying
    @Query("delete from DatosEquipoUsuario e where" +
            " e.usuario.id = ?1 and e.equipo.id = ?2"
            )
    public void eliminar(Long idU,Long idE);
}
