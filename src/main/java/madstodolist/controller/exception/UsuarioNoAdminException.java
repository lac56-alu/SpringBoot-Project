package madstodolist.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="Usuario no autorizado ya que no es admin")
public class UsuarioNoAdminException extends RuntimeException{
}
