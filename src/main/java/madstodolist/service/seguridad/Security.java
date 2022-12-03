package madstodolist.service.seguridad;

import madstodolist.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Security {
    @Autowired
    private JavaMailSender mail;
    public void sendVerificationEmail(Usuario user, String siteURL) {

    }
}
