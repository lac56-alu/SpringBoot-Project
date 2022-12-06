package madstodolist.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TareaRepository extends CrudRepository<Tarea, Long> {
    @Query("select t from Tarea t where" +
            " CONCAT(t.id,t.titulo) "
            + "like %?1%" +
            " and t.usuario.id" + "= ?2")
    public List<Tarea> busqueda(String busca,Long id);
}
