package madstodolist.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String s);
    @Query("SELECT u FROM Usuario u WHERE u.verificationCode = ?1")
    public Usuario findByVerificationCode(String code);
}
