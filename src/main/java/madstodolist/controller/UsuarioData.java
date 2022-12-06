package madstodolist.controller;

import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Email;
import java.util.Date;

public class UsuarioData {
    @Email
    private String email;
    private String nombre;
    private String password;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaNacimiento;

    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
