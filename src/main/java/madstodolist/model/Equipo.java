package madstodolist.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "equipos")
public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;


    private Long lider;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "equipo_usuario",
            joinColumns = { @JoinColumn(name = "fk_equipo") },
            inverseJoinColumns = {@JoinColumn(name = "fk_usuario")}
    )
    Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "equipo", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    Set<TareasEquipo> tareas = new HashSet<>();

    @OneToMany(mappedBy = "equipo", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    Set<DatosEquipoUsuario> datos = new HashSet<>();
    public Equipo() {}
    public Equipo(String nombre){
        this.nombre = nombre;
        this.descripcion = "";
    }
    public Equipo(String nombre, String descripcion){
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public Equipo(String nombre, String descripcion, Long lider) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.lider = lider;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public Set<Usuario> getUsuarios() {
        return usuarios;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public Long getLider() {
        return this.lider;
    }
    public void setLider(Long lider) {
        this.lider = lider;
    }
    public void addUsuario(Usuario usuario) {
        this.getUsuarios().add(usuario);
        usuario.getEquipos().add(this);
    }
    public void deleteUsuario(Usuario usuario){
        this.getUsuarios().remove(usuario);
        usuario.getEquipos().remove(this);
    }

    public Set<TareasEquipo> getTareas() {
        return tareas;
    }

    public void setTareas(Set<TareasEquipo> tareas) {
        this.tareas = tareas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        if (id != null && equipo.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, equipo.id);
        // sino comparamos por campos obligatorios
        return nombre.equals(equipo.nombre);
    }

    @Override
    public int hashCode() {
        // Generamos un hash basado en los campos obligatorios
        return Objects.hash(nombre);
    }
}
