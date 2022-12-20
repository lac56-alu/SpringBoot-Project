package madstodolist.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "datos_equipo_usuario")
public class DatosEquipoUsuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rol;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    public DatosEquipoUsuario(){
    }
    public DatosEquipoUsuario(Usuario us,Equipo eq,String rol){
        this.usuario= us;
        this.equipo = eq;
        this.rol = rol;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario(){
        return usuario;
    }
    public void setUsuario(Usuario u){
        this.usuario = u;
    }
    public Equipo getEquipo(){
        return equipo;
    }
    public void setEquipo(Equipo e){
        this.equipo = e;
    }
    public String getRol(){
        return rol;
    }
    public void setRol(String r){
        this.rol = r;
    }
}
