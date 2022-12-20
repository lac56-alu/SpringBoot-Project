package madstodolist.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface EquipoRepository extends CrudRepository<Equipo, Long> {
    Optional<Equipo> findById(Long id);
    public List<Equipo> findAll();

    @Query("select e from Equipo e where" +
            " CONCAT(e.id,e.nombre,e.descripcion) "
            + "like %?1%")
    public List<Equipo> busqueda(String busca);
}
