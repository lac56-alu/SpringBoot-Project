package madstodolist.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TareasEquipoRepository extends CrudRepository<TareasEquipo, Long> {
    @Query("select t from TareasEquipo t where" +
            " CONCAT(t.id,t.titulo) "
            + "like %?1%" +
            " and t.equipo.id" + "= ?2")
    public List<TareasEquipo> busqueda(String busca, Long id);
}
