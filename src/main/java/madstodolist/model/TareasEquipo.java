package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tareasequipo")
public class TareasEquipo implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum TareasEquipoStatus {
        POR_HACER,
        EN_PROCESO,
        FINALIZADA;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String titulo;

    @NotNull
    private String descripcion;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    @NotNull
    private String estado;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    public TareasEquipo() {
    }

    public TareasEquipo(Equipo equipo, String titulo, String descripcion, Date fecha) {
        this.equipo = equipo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = TareasEquipoStatus.POR_HACER.toString();
        equipo.getTareas().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TareasEquipo that = (TareasEquipo) o;
        if (id != null && that.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, that.id);
        return Objects.equals(id, that.id) && Objects.equals(titulo, that.titulo) && Objects.equals(descripcion, that.descripcion) && Objects.equals(fecha, that.fecha) && Objects.equals(equipo, that.equipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descripcion, fecha, equipo);
    }
}
